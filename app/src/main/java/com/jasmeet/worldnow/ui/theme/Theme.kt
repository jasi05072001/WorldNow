package com.jasmeet.worldnow.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    //color for button
    primary = darkButtonColor,
    //color for background
    background = darkBackGround,
    //color for text
    onPrimary = darkButtonTextColor,
    onSurface = darkTextColor

    )

private val LightColorScheme = lightColorScheme(
    //color for button
    primary = lightButtonColor,
    //color for background
    background = lightBackground,
    //color for text
    onPrimary = lightButtonTextColor ,
    onSurface = lightTextColor

    )

@Composable
fun WorldNowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if(!darkTheme) LightColorScheme else DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color(0xFF215776).toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false

            if (darkTheme){
                window.navigationBarColor = darkBackGround.toArgb()
            }
            else{
                window.navigationBarColor = Color(0xFF215776).toArgb()
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}