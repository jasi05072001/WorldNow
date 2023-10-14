package com.jasmeet.worldnow.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.api.LogDescriptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.jasmeet.worldnow.data.UserInfo
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
                    AppRouter.navigateTo(Screens.MainScreen1a)
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

fun returnCountryFromSharedPrefs(context: Context): List<String> {
    val sharedPrefs = context.getSharedPreferences("country", Context.MODE_PRIVATE)
    val country = sharedPrefs.getString("country", "").toString()
    val countryList = country.split(",")
    return removeSpecialCharacters(countryList)
}

fun removeSpecialCharacters(strings: List<String>): List<String> {
    val pattern = Regex("[^A-Za-z0-9 ]") // Define a regex pattern to match special characters

    return strings.map { originalString ->
        pattern.replace(originalString, "") .trim()// Replace special characters with an empty string
    }
}

//suspend fun getProfileImgUrlFromFirebase(): String = withContext(Dispatchers.IO) {
//    val userId = FirebaseAuth.getInstance().currentUser?.uid
//    var profileUrl: String
//
//    if (userId != null) {
//        try {
//            val usersCollection = FirebaseFirestore.getInstance().collection("users")
//            val documentSnapshot = usersCollection.document(userId).get().await()
//
//            Log.d("Info", "getProfileImgUrlFromFirebase:  ${documentSnapshot.data}")
//            val userInfo = documentSnapshot.toObject<UserInfo>()
//            profileUrl = userInfo?.imgUrl.toString()
//
//        } catch (e: Exception) {
//            // Log the error for debugging
//            Log.e("Error", "Error checking user document: ${e.message}", e)
//            profileUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
//        }
//    } else {
//        profileUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
//    }
//
//    return@withContext profileUrl
//}

suspend fun getProfileImgUrlFromFirebase(): String = withContext(Dispatchers.IO) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var profileUrl: String

    if (userId != null) {
        try {
            val usersCollection = FirebaseFirestore.getInstance().collection("users")
            val documentSnapshot = usersCollection.document(userId).get().await()

            Log.d("Data", "getProfileImgUrlFromFirebase:  ${documentSnapshot.data}")

            // Get the imgUrl from the documentSnapshot
            val imgUrl = documentSnapshot.getString("photoUrl")

            Log.d("Data", "getProfileImgUrlFromFirebase:  $imgUrl")

            // Check if the document exists and contains the "imgUrl" field
            if (documentSnapshot.exists() && documentSnapshot.contains("imgUrl")) {
                profileUrl = documentSnapshot.getString("photoUrl") ?: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
                Log.d("TAGER", "getProfileImgUrlFromFirebase: $profileUrl")
            } else {
                profileUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
            }
        } catch (e: Exception) {
            // Log the error for debugging
            Log.e("Error", "Error checking user document: ${e.message}", e)
            profileUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
        }
    } else {
        profileUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
    }

    // Log the imgUrl
    Log.d("Info", "Profile Image URL: $profileUrl")

    return@withContext profileUrl
}


