package com.frost.woo_withemark.ui.home

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.woo_withemark.R
import com.frost.woo_withemark.databinding.ActivityHomeBinding
import com.frost.woo_withemark.databinding.ActivityMainBinding
import com.frost.woo_withemark.extensions.clearPrefs
import com.frost.woo_withemark.extensions.showToast
import com.frost.woo_withemark.extensions.signOut
import com.frost.woo_withemark.models.WooProduct
import com.frost.woo_withemark.ui.detail.DetailActivity
import com.frost.woo_withemark.ui.login.MainActivity
import com.frost.woo_withemark.ui.utils.ItemAdapter
import com.frost.woo_withemark.ui.utils.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private var loadingDialog = LoadingDialog()

    companion object {
        fun start(activity: Activity){
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog.show(supportFragmentManager)
        viewModel.getProducts(getString(R.string.key), getString(R.string.customer))
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.productListLiveData.observe(this) { handleProduct(it) }
    }

    private fun handleProduct(productList: List<WooProduct>?) {
        loadingDialog.dismiss()
        productList
            ?.let { setAdapter(it) }
            ?:run { showToast(this, getString(R.string.error_data)) }
    }

    private fun setAdapter(productList: List<WooProduct>) {
        val adapter = ItemAdapter(productList, this@HomeActivity)
        with(binding){
            postListrecyclerView.layoutManager = GridLayoutManager(this@HomeActivity, 2)
            postListrecyclerView.adapter = adapter
        }
        adapter.onProductClickCallback = { DetailActivity.start(this, it.id!!) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        signOut()
        clearPrefs()
        MainActivity.start(this)
        finish()
    }
}