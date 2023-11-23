package com.jasmeet.worldnow

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.jasmeet.worldnow.dataStore.DataStoreUtil
import com.jasmeet.worldnow.screens.mainScreen.MainApp
import com.jasmeet.worldnow.ui.theme.WorldNowTheme
import com.jasmeet.worldnow.viewModels.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()
    private lateinit var dataStoreUtil: DataStoreUtil
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        dataStoreUtil = DataStoreUtil(applicationContext)

        val systemTheme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> { true }
            Configuration.UI_MODE_NIGHT_NO -> { false }
            else -> { false }
        }

        super.onCreate(savedInstanceState)
        setContent {

            val theme = dataStoreUtil.getTheme(systemTheme).collectAsState(initial = systemTheme)
            Log.d("Theme", "onCreate: ${theme.value}")

            WorldNowTheme(theme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MainApp(dataStoreUtil, themeViewModel,theme.value)
                }
            }
        }
    }


}


