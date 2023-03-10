package com.frost.woo_withemark.models

import com.google.firebase.auth.FirebaseUser

data class User(
    var email: String?=null,
    var nombre: String?=null,
    var rol: String?=null,
    var photo: String?=null,
    var empresa: String?=null
) {

    fun convert(user: FirebaseUser): User{
        this.email = user.email.toString()
        this.nombre = user.displayName.toString()
        this.rol = "admin"
        this.photo = user.photoUrl.toString()
        return this
    }

}
