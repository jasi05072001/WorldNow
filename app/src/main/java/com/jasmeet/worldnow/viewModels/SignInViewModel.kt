package com.jasmeet.worldnow.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message
    val isLoading = mutableStateOf(false)

    private val debounceTimeMillis = 5000L
    private var lastErrorShownTime = 0L

    private val state = MutableStateFlow<Boolean>(false)
    val stateFlow: StateFlow<Boolean> = state

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

    fun signIn(email: String, password: String) {
        isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    _message.value = "Welcome back!"
                    AppRouter.navigateTo(Screens.MainScreen1a)
                }
            }
            .addOnFailureListener {
                isLoading.value = false
                _message.value = it.message
                setErrorMessage(it.message)
            }
    }

    fun resetPasswordLink(email: String) {
        isLoading.value = true
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoading.value = false
                    state.value = true
                } else {
                    _message.value = task.exception?.message ?: "Unknown error occurred."
                    isLoading.value = false
                    state.value = false
                    setErrorMessage(task.exception?.message ?: "Unknown error occurred.")
                }
            }
    }

    fun resetResetPasswordState() {
        state.value = false
    }

}
