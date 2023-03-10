package com.frost.woo_withemark.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.frost.woo_withemark.R
import com.frost.woo_withemark.databinding.ActivityMainBinding
import com.frost.woo_withemark.extensions.getEmailPref
import com.frost.woo_withemark.extensions.savePref
import com.frost.woo_withemark.extensions.showAlert
import com.frost.woo_withemark.extensions.signInWithCredential
import com.frost.woo_withemark.models.User
import com.frost.woo_withemark.ui.home.HomeActivity
import com.frost.woo_withemark.ui.utils.LoadingDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityMainBinding
    private var loadingDialog = LoadingDialog()

    companion object {
        const val GOOGLE_SIGN_IN = 100
        fun start(activity: Activity){
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.googleButton.setOnClickListener { setGoogleWidget() }
        checkSession()
    }

    private fun checkSession() {
        getEmailPref()?.let { HomeActivity.start(this) }
    }

    private fun saveAndContinue(user: User) {
        viewModel.saveInDatabase(user)
        savePref(user)
        loadingDialog.dismiss()
        HomeActivity.start(this)
        finish()
    }

    private fun setGoogleWidget() {
        val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConfig)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            loadingDialog.show(supportFragmentManager)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                account?.let { account ->
                    signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                val user = User().convert(it.result.user!!)
                                saveAndContinue(user)
                            }else {
                                showAlert()
                            }
                        }
                }
            }catch (e: ApiException){
                showAlert()
            }
        }
    }
}