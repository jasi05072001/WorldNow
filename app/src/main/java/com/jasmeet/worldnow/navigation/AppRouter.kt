package com.jasmeet.worldnow.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screens{

    data object SplashScreen :Screens()
    data object SignInScreen :Screens()
    data object SignUpScreen :Screens()
    data object IntroScreen :Screens()
    data object HomeScreen :Screens()
}
object AppRouter{
    var currentScreen: MutableState<Screens> = mutableStateOf(Screens.SplashScreen)

    fun navigateTo(destination: Screens){
        currentScreen.value = destination
    }
}