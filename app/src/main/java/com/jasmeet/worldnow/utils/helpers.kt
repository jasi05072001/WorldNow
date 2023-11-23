package com.jasmeet.worldnow.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

fun saveUserInfoToSharedPrefs(email: String, context: Context) {
    val sharedPrefs = context.getSharedPreferences("email", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()
    editor.putString("email", email)
    editor.apply()

}

fun returnEmailFromSharedPrefs(context: Context): String {
    val sharedPrefs = context.getSharedPreferences("email", Context.MODE_PRIVATE)
    return sharedPrefs.getString("email", "").toString()
}

fun saveCountryToSharedPrefs(country: List<String>, context: Context) {
    // save email to shared prefs
    val sharedPrefs = context.getSharedPreferences("country", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()
    editor.putString("country", country.toString())
    editor.apply()

}

suspend fun getSelectedInterests(): List<String> = withContext(Dispatchers.IO) {

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    return@withContext if (userId != null){
        val userCollection = FirebaseFirestore.getInstance().collection("users")
        val docSnapShot = userCollection.document(userId).get().await()

        val interests = docSnapShot.get("interest") as List<*>
        interests as List<String>
    }
    else{
        listOf()
    }
}

fun removeWhitespaces(input: String): String {
    return input.replace("\\s".toRegex(), "")
}
fun removeBrackets(input: String): String {
    return input.replace("[\\[\\]{}]", "")
}


