package com.jasmeet.worldnow.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.worldnow.data.news.News
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.repository.NewsRepository
import com.jasmeet.worldnow.repository.SavedNewsRepository
import com.jasmeet.worldnow.room.NewsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: NewsRepository,
    private val savedNewsRepository: SavedNewsRepository
) :ViewModel() {

    private val _newsList = MutableLiveData<List<NewsData>>()
    val newsList: LiveData<List<NewsData>> get() = _newsList


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

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
                _newsList.value = listOf(newsData)
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

    fun deleteNewsDataById(savedAt:Long){
        viewModelScope.launch {
            try{
                savedNewsRepository.deleteNewsDataById(savedAt)
            }catch (e:Exception){
                _errorMessage.value = "Failed to delete news: ${e.message}"
            }
        }
    }

}