package com.jasmeet.worldnow.screens.home.settings.update

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jasmeet.worldnow.appComponents.ButtonComponent
import com.jasmeet.worldnow.appComponents.Space
import com.jasmeet.worldnow.data.InterestList
import com.jasmeet.worldnow.data.interestList
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.navigation.SystemBackButtonHandler
import com.jasmeet.worldnow.screens.settingAccount.selectInterest.InterestItem
import com.jasmeet.worldnow.ui.theme.inter
import com.jasmeet.worldnow.viewModels.NewsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UpdateInterestsScreen() {

    val isScreenComing = remember{
        mutableStateOf(false)
    }
    AnimatedVisibility(
        visible = isScreenComing.value,
        enter =  fadeIn() +
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        950,
                        0,
                        LinearEasing
                    )
                ) ,
        modifier = Modifier.background(
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF215776),
                    Color(0xFF32989b)
                )

            ))
    ) {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            UpdateInterestSelectionLayout()

        }


    }

    LaunchedEffect(Unit ){
        delay(10)
        isScreenComing.value = true
    }

    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.SettingsScreen)

    }


}

@Composable
fun UpdateInterestSelectionLayout() {
    val newsViewModel:NewsViewModel = hiltViewModel()

    val selectedItemCount = rememberSaveable(key = "selectedItemCount") {
        mutableIntStateOf(0)
    }

    val selectedItems = rememberSaveable(key = "selectedItems") {
        mutableStateOf(mutableListOf<InterestList>())
    }

    val selectedInterests = rememberSaveable {
        mutableStateOf(mutableListOf<String>())
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Select Your Interests(at-least 3)",
            fontSize = 20.sp,
            fontFamily = inter,
            fontWeight = FontWeight(800),
            color = MaterialTheme.colorScheme.primary
        )
        Space(height = 16.dp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .weight(0.25f),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ){
            items(interestList.size){ index ->
                InterestItem(
                    interest = interestList[index],
                    selectedItemCount = selectedItemCount,
                    selectedItems = selectedItems
                )
            }

        }


        ButtonComponent(
            onclick = {
                selectedItems.value.forEach {
                    selectedInterests.value.add(it.interestName)
                }

                coroutineScope.launch {
                    newsViewModel.updateInterests(selectedInterests.value)
                    delay(500)
                    AppRouter.navigateTo(Screens.SettingsScreen)
                }


            },
            text = "Continue",
            isEnabled = selectedItemCount.intValue >= 3,
        )

        Space(height = 15.dp)
    }

}
