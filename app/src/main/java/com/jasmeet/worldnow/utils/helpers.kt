package com.jasmeet.worldnow.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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

fun saveUserInfo(context: Context) {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId != null) {
        val usersCollection = db.collection("users")

        // if the user document already exists
        usersCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (documentSnapshot.exists()) {
                   AppRouter.navigateTo(Screens.HomeScreen)
                } else {
                    AppRouter.navigateTo(Screens.SelectingCountryScreen)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Failure", "Error checking user document: ${exception.message}")
                showToast(context, "${exception.message}")
            }
    } else {
        // User is not authenticated, handle the case accordingly
        Log.d("Info", "User not authenticated")
    }
}
