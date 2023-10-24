package com.jasmeet.worldnow.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.worldnow.data.news.News
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.repository.NewsRepository

class NewsViewModel(
    private val firebaseAuth :FirebaseAuth= FirebaseAuth.getInstance(),
    private val repository: NewsRepository = NewsRepository()
):ViewModel() {

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

}