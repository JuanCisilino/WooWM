package com.frost.woo_withemark.extensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.frost.woo_withemark.R
import com.frost.woo_withemark.models.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth


fun Activity.signInWithCredential(credential: AuthCredential) =
    FirebaseAuth.getInstance().signInWithCredential(credential)

fun Activity.signOut() = FirebaseAuth.getInstance().signOut()

fun Activity.showAlert(){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.error))
    builder.setMessage(getString(R.string.error_message))
    builder.setPositiveButton(getString(R.string.ok), null)
    val dialog = builder.create()
    dialog.show()
}

fun Activity.getPref() = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

fun Activity.savePref(user: User){
    val prefs = getPref().edit()
    prefs.putString(R.string.email.toString(), user.email)
    prefs.putString(R.string.name.toString(), user.nombre)
    prefs.putString(R.string.photo.toString(), user.photo)
    prefs.putString(R.string.rol.toString(), user.rol)
    prefs.putString(R.string.empresa.toString(), user.empresa)
    prefs.apply()
}

fun Activity.getEmailPref(): String?{
    val prefs = getPref()
    return prefs.getString(R.string.email.toString(), null)
}

fun Activity.clearPrefs(){
    val prefs = getPref()
    prefs?.edit()?.clear()?.apply()
}

fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}