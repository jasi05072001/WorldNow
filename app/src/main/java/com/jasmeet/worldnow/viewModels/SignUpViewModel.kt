package com.jasmeet.worldnow.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jasmeet.worldnow.data.UserInfo
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.utils.saveUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message
    val isLoading = mutableStateOf(false)

    private val debounceTimeMillis = 5000L
    private var lastErrorShownTime = 0L


    init {
        viewModelScope.launch {
            while (true) {
                delay(debounceTimeMillis)
                setErrorMessage(null)
            }
        }
    }

    fun setErrorMessage(errorMessage: String?) {
        val currentTimeMillis = System.currentTimeMillis()
        if (errorMessage != null && (currentTimeMillis - lastErrorShownTime) >= debounceTimeMillis) {
            _message.value = errorMessage
            lastErrorShownTime = currentTimeMillis
        } else if (errorMessage == null) {
            _message.value = null
            lastErrorShownTime = 0L
        }
    }

    fun signUp(email: String, password: String) {
        isLoading.value = true

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            setErrorMessage("Please fill all the fields")
            isLoading.value = false
            return
        }
        else if (password.length < 6) {
            setErrorMessage("Password must be at least 6 characters")
            isLoading.value = false
            return
        }
        else if (!email.contains("@")) {
            setErrorMessage("Please enter a valid email address")
            isLoading.value = false
            return
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AppRouter.navigateTo(Screens.SelectingCountryScreen)
                        isLoading.value = false
                    }
                }
        }
            .addOnFailureListener {
                isLoading.value = false
                _message.value = it.message
                setErrorMessage(it.message)
            }
    }



}
