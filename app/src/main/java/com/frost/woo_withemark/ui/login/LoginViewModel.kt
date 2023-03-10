package com.frost.woo_withemark.ui.login

import androidx.lifecycle.ViewModel
import com.frost.woo_withemark.models.User
import com.frost.woo_withemark.repositories.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dbRepository: FirebaseRepository): ViewModel() {

    var existingUser : Boolean = false
    private set
    var user: User? = null
    private set

    fun saveInDatabase(user: User){
        dbRepository.saveUser(user)
    }

    fun checkUser(email: String){
        dbRepository.getUser(email).addOnCompleteListener {
            existingUser = if (it.isSuccessful) {
                user = it.result.toObject(User::class.java)
                true
            } else {
                false
            }
        }
    }
}