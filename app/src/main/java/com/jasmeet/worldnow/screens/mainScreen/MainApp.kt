package com.jasmeet.worldnow.screens.mainScreen

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.jasmeet.worldnow.dataStore.DataStoreUtil
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.screens.home.HomeScreenLayout
import com.jasmeet.worldnow.screens.home.categories.CategoriesView
import com.jasmeet.worldnow.screens.home.categories.DetailedScreen
import com.jasmeet.worldnow.screens.home.saved.SavedDetailedScreen
import com.jasmeet.worldnow.screens.home.saved.SavedScreen
import com.jasmeet.worldnow.screens.home.settings.SettingsScreen
import com.jasmeet.worldnow.screens.home.settings.update.UpdateInterestsScreen
import com.jasmeet.worldnow.screens.onBoarding.forgotPasswordScreen.ForgotPasswordScreen
import com.jasmeet.worldnow.screens.onBoarding.introScreen.IntroScreen
import com.jasmeet.worldnow.screens.onBoarding.signInScreen.SignInScreen
import com.jasmeet.worldnow.screens.onBoarding.signUpScreen.SignUpScreen
import com.jasmeet.worldnow.screens.onBoarding.splashScreen.SplashScreen
import com.jasmeet.worldnow.screens.settingAccount.profile.ProfileScreen
import com.jasmeet.worldnow.screens.settingAccount.selectCountry.CountrySelectionScreen
import com.jasmeet.worldnow.screens.settingAccount.selectInterest.InterestSelectionScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp(dataStoreUtil: DataStoreUtil, value: Boolean) {

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
            val forgotPasswordScreenVisible = currentScreen.value is Screens.ForgotPasswordScreen
            val countrySelectionScreenVisible = currentScreen.value is Screens.SelectingCountryScreen
            val interestSelectionScreenVisible = currentScreen.value is Screens.SelectingInterestScreen
            val profileScreenVisible = currentScreen.value is Screens.ProfileScreen
            val detailedScreenVisible = currentScreen.value is Screens.DetailedScreen
            val homeScreenVisible = currentScreen.value is Screens.HomeScreen
            val categoriesScreenVisible = currentScreen.value is Screens.CategoriesScreen
            val savedScreenVisible = currentScreen.value is Screens.SavedScreen
            val savedDetailedScreenVisible = currentScreen.value is Screens.SavedDetailsArticlesScreen
            val settingsScreenVisible = currentScreen.value is Screens.SettingsScreen
            val updateInterestsScreenVisible = currentScreen.value is Screens.UpdateInterestsScreen


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
                visible = categoriesScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                CategoriesView()
            }
            AnimatedVisibility(
                visible = detailedScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                DetailedScreen()
            }
            AnimatedVisibility(
                visible = homeScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                HomeScreenLayout()
            }

            AnimatedVisibility(
                visible = savedScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                SavedScreen()
            }
            AnimatedVisibility(
                visible = savedDetailedScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                SavedDetailedScreen()
            }
            AnimatedVisibility(
                visible = settingsScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                SettingsScreen(dataStoreUtil,value)
            }
            AnimatedVisibility(
                visible = updateInterestsScreenVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                UpdateInterestsScreen()
            }
        }
    }
}


