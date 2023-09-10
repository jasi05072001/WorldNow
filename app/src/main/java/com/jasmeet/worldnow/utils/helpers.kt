package com.jasmeet.worldnow.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jasmeet.worldnow.data.UserInfo
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun saveUserInfo(userInfo: UserInfo) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
        .set(userInfo)
        .addOnSuccessListener {
            AppRouter.navigateTo(Screens.SelectingCountryScreen)
        }
        .addOnFailureListener { exception ->
            Log.d("Failure", "saveUserInfo: ${exception.message} ")
        }
}
