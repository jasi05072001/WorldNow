package com.jasmeet.worldnow.screens.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.screens.home.mainScreenPage1.MainScreenPage1
import com.jasmeet.worldnow.screens.onBoarding.forgotPasswordScreen.ForgotPasswordScreen
import com.jasmeet.worldnow.screens.onBoarding.introScreen.IntroScreen
import com.jasmeet.worldnow.screens.onBoarding.signInScreen.SignInScreen
import com.jasmeet.worldnow.screens.onBoarding.signUpScreen.SignUpScreen
import com.jasmeet.worldnow.screens.onBoarding.splashScreen.SplashScreen
import com.jasmeet.worldnow.screens.settingAccount.profile.ProfileScreen
import com.jasmeet.worldnow.screens.settingAccount.selectCountry.CountrySelectionScreen
import com.jasmeet.worldnow.screens.settingAccount.selectInterest.InterestSelectionScreen

@Composable
fun MainApp() {

    Surface(
        modifier = Modifier
            .fillMaxSize()

    ){
        val currentScreen = AppRouter.currentScreen

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val splashScreenVisible = currentScreen.value is Screens.SplashScreen
            val signUpScreenVisible = currentScreen.value is Screens.SignUpScreen
            val signInScreenVisible = currentScreen.value is Screens.SignInScreen
            val introScreenVisible = currentScreen.value is Screens.IntroScreen
            val mainScreen1aVisible = currentScreen.value is Screens.MainScreen1a
            val forgotPasswordScreenVisible = currentScreen.value is Screens.ForgotPasswordScreen
            val countrySelectionScreenVisible = currentScreen.value is Screens.SelectingCountryScreen
            val interestSelectionScreenVisible = currentScreen.value is Screens.SelectingInterestScreen
            val profileScreenVisible = currentScreen.value is Screens.ProfileScreen

            AnimatedVisibility(
                visible= splashScreenVisible,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
            ) {
                SplashScreen()
            }

            AnimatedVisibility(
                visible = signUpScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                SignUpScreen()
            }
            AnimatedVisibility(
                visible = signInScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
            ) {
                SignInScreen()
            }

            AnimatedVisibility(
                visible = introScreenVisible,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                exit = fadeOut() + slideOutHorizontally (targetOffsetX = { it })
            ) {
                IntroScreen()
            }


            AnimatedVisibility(
                visible = forgotPasswordScreenVisible,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                exit = fadeOut() + slideOutHorizontally (targetOffsetX = { it })
            ) {
                ForgotPasswordScreen()
            }

            AnimatedVisibility(
                visible = countrySelectionScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
            ) {
                CountrySelectionScreen()
            }
            AnimatedVisibility(
                visible = interestSelectionScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                InterestSelectionScreen()
            }

            AnimatedVisibility(
                visible = profileScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                ProfileScreen()

            }

            AnimatedVisibility(
                visible = mainScreen1aVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                MainScreenPage1()
            }
        }
    }
}


