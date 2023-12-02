package com.jasmeet.worldnow.viewModels

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jasmeet.worldnow.data.news.News
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.repository.NewsRepository
import com.jasmeet.worldnow.repository.SavedNewsRepository
import com.jasmeet.worldnow.room.NewsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: NewsRepository,
    private val savedNewsRepository: SavedNewsRepository
) :ViewModel() {

    private val _newsList = MutableLiveData<List<NewsData>>()
    val newsList: LiveData<List<NewsData>> get() = _newsList


    private val _singleNewsItem = MutableLiveData<NewsData?>()
    val singleNewsItem: LiveData<NewsData?> get() = _singleNewsItem


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    //this function will return the pair of profile image and name
    suspend fun getProfileImgAndName(): Pair<String, String> = withContext(Dispatchers.IO) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        return@withContext if (userId != null){
            val userCollection = FirebaseFirestore.getInstance().collection("users")
            val docSnapShot = userCollection.document(userId).get().await()

            val name = docSnapShot.getString("name").toString()
            val photoUrl = docSnapShot.getString("photoUrl").toString()

            Pair(name, photoUrl)
        }
        else{
            Pair("","")
        }
    }


    suspend fun getCountry(): String = withContext(Dispatchers.IO) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        return@withContext if (userId != null){
            val userCollection = FirebaseFirestore.getInstance().collection("users")
            val docSnapShot = userCollection.document(userId).get().await()

            val country = docSnapShot.getString("country").toString()
            country
        }
        else{
            ""
        }
    }
    suspend fun updateName(name: String) = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userCollection = FirebaseFirestore.getInstance().collection("users")
            userCollection.document(userId).update("name", name).await()
        }
    }

    suspend fun updateProfileImg(
        imgUrl: Uri,
        contentResolver: ContentResolver
    ) = withContext(Dispatchers.IO) {

        val userId = auth.currentUser?.uid
        val storageRef = Firebase.storage.reference.child("profileImages")
        val imageStream: InputStream? = imgUrl.let { contentResolver.openInputStream(it) }

        // Generate a unique name for the image in Firebase Storage
        val imageName = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$userId/$imageName")

        // Upload the image to Firebase Storage
        val uploadTask = imageRef.putStream(imageStream!!)
        uploadTask.addOnSuccessListener { _ ->
            // Image uploaded successfully, now get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()

                // Update user's profile with the new image URL
                auth.currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(downloadUrl))
                        .build()
                )?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (userId != null) {
                            val userCollection = FirebaseFirestore.getInstance().collection("users")
                            viewModelScope.launch {
                                userCollection.document(userId).update("photoUrl", downloadUrl).await()
                            }
                        }
                    } else {
                        _errorMessage.value = task.exception?.message
                    }
                }
            }.addOnFailureListener { exception ->
                _errorMessage.value = exception.message.toString()
            }
        }.addOnFailureListener { exception ->
            _errorMessage.value = exception.message.toString()
        }
    }

    suspend fun updateCountry(country: String) = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userCollection = FirebaseFirestore.getInstance().collection("users")
            userCollection.document(userId).update("country", country).await()
        }
    }

    suspend fun updateInterests(interests :List<String>) = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userCollection = FirebaseFirestore.getInstance().collection("users")
            userCollection.document(userId).update("interest", interests).await()
        }
    }



    fun getNews(
        query :String,
        page :Int,
        successCallBack :(response : News?) ->Unit,
        failureCallback: (error: String) -> Unit,
        pagSize :Int = 70,
        from:String
    ){
        repository.getNews(query,page,successCallBack,failureCallback,pagSize,from)
    }

    fun getRandomNews(
        query :String,
        page :Int,
        successCallBack :(response : News?) ->Unit,
        failureCallback: (error: String) -> Unit,
        pagSize :Int = 70,
        from:String
    ){
        repository.getNews(query,page,successCallBack,failureCallback,pagSize,from)
    }


    fun logout(googleSignInClient: GoogleSignInClient) {
        // Sign out of Firebase Authentication
        FirebaseAuth.getInstance().signOut()

        // Sign out of Google Sign-In
        googleSignInClient.signOut()
        AppRouter.navigateTo(Screens.SignUpScreen)
    }

    fun insertNews(newsData :NewsData){
        viewModelScope.launch {
            try{
                savedNewsRepository.insert(newsData)
            }catch (e:Exception){
                _errorMessage.value = e.message
            }
        }
    }

    fun getNewsDataById(savedAt:Long){
        viewModelScope.launch {
            try{
                val newsData = savedNewsRepository.getNewsDataById(savedAt)
                _singleNewsItem.postValue(newsData)
            }catch (e:Exception){
                _errorMessage.value = "Failed to save news: ${e.message}"
            }
        }
    }

    fun getAllNewsData(){
        viewModelScope.launch{
            try {
                val newsDataList = savedNewsRepository.getAllNewsData()
                _newsList.value = newsDataList
            }catch (e:Exception){
                _errorMessage.value = "Failed to retrieve news: ${e.message}"
            }

        }
    }

    fun deleteNewsDataById(url:String){
        viewModelScope.launch {
            try{
                savedNewsRepository.deleteNewsDataById(url)
                getAllNewsData()
            }catch (e:Exception){
                _errorMessage.value = "Failed to delete news: ${e.message}"
            }
        }
    }


}