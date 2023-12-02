package com.jasmeet.worldnow.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jasmeet.worldnow.data.news.Article
import com.jasmeet.worldnow.room.NewsData

sealed class Screens{
    data object DetailedScreen : Screens()
    data object SplashScreen :Screens()
    data object SignInScreen :Screens()
    data object SignUpScreen :Screens()
    data object  ForgotPasswordScreen :Screens()
    data object IntroScreen :Screens()
    data object SelectingCountryScreen :Screens()
    data object SelectingInterestScreen :Screens()
    data object ProfileScreen :Screens()
    data object HomeScreen :Screens()
    data object CategoriesScreen :Screens()
    data object SavedScreen :Screens()
    data object SavedDetailsArticlesScreen :Screens()
    data object SettingsScreen :Screens()
    data object UpdateInterestsScreen :Screens()


}
object AppRouter{
    var currentScreen: MutableState<Screens> = mutableStateOf(Screens.SplashScreen)

    var selectedCountry by mutableStateOf<String?>(null)

    var selectedInterest by mutableStateOf<List<String>?>(null)

    var email by mutableStateOf<String?>(null)

    var detailedArticles by mutableStateOf<Article?>(null)

    var savedDetailedArticles by mutableStateOf<NewsData?>(null)

    fun navigateTo(destination: Screens){
        currentScreen.value = destination
    }
}