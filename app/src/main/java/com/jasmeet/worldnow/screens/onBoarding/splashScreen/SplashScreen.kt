package com.jasmeet.worldnow.screens.onBoarding.splashScreen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.ui.theme.helventica
import com.jasmeet.worldnow.viewModels.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = viewModel()
) {
    val isUserLoggedIn = splashViewModel.isUserLoggedIn

    val scale = remember {
        Animatable(initialValue = 0f)
    }


    val height = LocalConfiguration.current.screenHeightDp.dp

    LaunchedEffect(
        key1 = true,
        block = {
            scale.animateTo(
                targetValue = 0.95f,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = {
                        OvershootInterpolator(0.8f).getInterpolation(it)
                    }
                )

            )

            splashViewModel.checkForActiveSession()
            delay(1500)

            if (isUserLoggedIn.value == true) {
                AppRouter.navigateTo(Screens.HomeScreen)

            }else {
                AppRouter.navigateTo(Screens.IntroScreen)
            }

        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF215776),
                        Color(0xFF32989b)
                    )
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .height(height = height / 3)
                .fillMaxSize()
                .scale(scale.value)
        )
        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "WORLD NOW",
            color = Color.White,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontFamily = helventica,
            fontWeight = FontWeight.W700,
            modifier = Modifier
                .scale(
                    scale.value*1.25f
                )
        )
    }
}