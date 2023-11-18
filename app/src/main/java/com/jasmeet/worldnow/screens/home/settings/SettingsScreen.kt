package com.jasmeet.worldnow.screens.home.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jasmeet.worldnow.dataStore.DataStoreUtil
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.navigation.SystemBackButtonHandler
import com.jasmeet.worldnow.viewModels.ThemeViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(dataStoreUtil: DataStoreUtil, themeViewModel: ThemeViewModel) {


    var switchState by remember {themeViewModel.isDarkThemeEnabled }


    Column(modifier = Modifier.fillMaxSize()) {
        MySwitch(
            switchState,
            onCheckedChange =  {
            switchState = it
        },
            dataStoreUtil = dataStoreUtil
        )
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.HomeScreen)
    }
}

@Composable
fun MySwitch(
    isCheck: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    dataStoreUtil: DataStoreUtil
) {
    val coroutineScope = rememberCoroutineScope()

    Switch(
        checked = isCheck,
        onCheckedChange = {
            onCheckedChange(it)
            coroutineScope.launch {
                dataStoreUtil.saveTheme(it)
            }
        },
        thumbContent = {
            if (isCheck) {
                Icon(
                    imageVector = Icons.Outlined.DarkMode,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.LightMode,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        },
    )
}

