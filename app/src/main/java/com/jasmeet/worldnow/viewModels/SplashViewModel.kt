package com.jasmeet.worldnow.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SplashViewModel :ViewModel(){

    val isUserLoggedIn: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    fun checkForActiveSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {

            Log.d("Valid session", "checkForActiveSession: ")
            isUserLoggedIn.value = true
        } else {
            Log.d("Invalid session", "Not checkForActiveSession: ")
            isUserLoggedIn.value = false
        }
    }
}