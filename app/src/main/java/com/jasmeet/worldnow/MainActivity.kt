package com.jasmeet.worldnow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.jasmeet.worldnow.screens.mainScreen.MainApp
import com.jasmeet.worldnow.screens.test.LazyColumnWithFavorites
import com.jasmeet.worldnow.screens.test.MainUi
import com.jasmeet.worldnow.screens.test.MainUi2
import com.jasmeet.worldnow.screens.test.MainUi3
import com.jasmeet.worldnow.ui.theme.WorldNowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorldNowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}


