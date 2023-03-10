package com.frost.woo_withemark.ui.login

import androidx.lifecycle.ViewModel
import com.frost.woo_withemark.models.User
import com.frost.woo_withemark.repositories.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dbRepository: FirebaseRepository): ViewModel() {

    fun saveInDatabase(user: User){
        dbRepository.saveUser(user)
    }
}