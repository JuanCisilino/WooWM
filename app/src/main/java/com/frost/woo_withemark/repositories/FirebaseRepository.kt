package com.frost.woo_withemark.repositories

import com.frost.woo_withemark.models.User
import com.google.firebase.firestore.CollectionReference

class FirebaseRepository(private val db: CollectionReference) {

    fun getUser(email: String) = db.document(email).get()

    fun saveUser(user: User) {
        db.document(user.email!!).set(
            hashMapOf(
                "name" to user.nombre,
                "email" to user.email,
                "photo" to user.photo,
                "rol" to user.rol
            )
        )
    }
}