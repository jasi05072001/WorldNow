package com.jasmeet.worldnow.viewModels

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jasmeet.worldnow.data.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(private val db:FirebaseFirestore):ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message
    val isLoading = mutableStateOf(false)

    private val debounceTimeMillis = 5000L
    private var lastErrorShownTime = 0L

    private val _state = MutableStateFlow<Boolean?>(false)
    val state :StateFlow<Boolean?> = _state


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

    fun saveUserInfo(
        name: String,
        email: String,
        userName: String,
        imageUri: Uri,
        country: String,
        interest: List<String>,
        contentResolver: ContentResolver
    ) {
        isLoading.value = true
        val storageRef = Firebase.storage.reference.child("profileImages")
        val imageStream: InputStream? = contentResolver.openInputStream(imageUri)

        imageStream?.use { inputStream ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, len)
            }
            val data = byteArrayOutputStream.toByteArray()

            storageRef.putBytes(data)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        val userDetails = Profile(
                            name = name,
                            email = email,
                            userName = userName,
                            photoUrl = downloadUrl,
                            country = country,
                            interest = interest
                        )
                        db.collection("users")
                            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                            .set(userDetails)
                            .addOnSuccessListener {
                                _message.value = "Profile Updated Successfully"
                                isLoading.value = false
                                _state.value = true
                            }
                            .addOnFailureListener {
                                _message.value = "Profile Update Failed"
                                isLoading.value = false
                                setErrorMessage(it.message ?: "Unknown error occurred.")

                            }
                    }
                }
                .addOnFailureListener {
                    _message.value = "Profile Update Failed"
                    isLoading.value = false
                    setErrorMessage(it.message ?: "Unknown error occurred.")
                }
        }
    }

}