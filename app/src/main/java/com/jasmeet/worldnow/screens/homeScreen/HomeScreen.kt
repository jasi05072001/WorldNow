package com.jasmeet.worldnow.screens.homeScreen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Button(
            onClick = {
                logOut(context = context)

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