package com.jasmeet.worldnow.screens.homeScreen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.viewModels.InterestsViewModel

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val interestsViewModel : InterestsViewModel = hiltViewModel()
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Button(
            onClick = {
//                logOut(context = context)
                val noob = interestsViewModel.getChoices()
                Log.d("Values", "HomeScreen: $noob")

            }
        ) {
            Text(text = "log out")
        }
    }

}

fun logOut(context: Context) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val googleSignInClient =
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
    firebaseAuth.signOut()
    googleSignInClient.signOut().addOnCompleteListener {
        AppRouter.navigateTo(Screens.SignUpScreen)
    }
    val authStateListener = FirebaseAuth.AuthStateListener {
        if (it.currentUser == null) {
            AppRouter.navigateTo(Screens.SignUpScreen)
        }
    }
    firebaseAuth.addAuthStateListener(authStateListener)
}