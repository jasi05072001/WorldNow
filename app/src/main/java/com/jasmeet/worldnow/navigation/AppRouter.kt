package com.jasmeet.worldnow.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class Screens{

    data object SplashScreen :Screens()
    data object SignInScreen :Screens()
    data object SignUpScreen :Screens()
    data object  ForgotPasswordScreen :Screens()
    data object IntroScreen :Screens()
    data object SelectingCountryScreen :Screens()
    data object SelectingInterestScreen :Screens()
    data object HomeScreen :Screens()
    data object ProfileScreen :Screens()
}
object AppRouter{
    var currentScreen: MutableState<Screens> = mutableStateOf(Screens.SplashScreen)

    var selectedCountry by mutableStateOf<String?>(null)

    fun navigateTo(destination: Screens){
        currentScreen.value = destination
    }
}