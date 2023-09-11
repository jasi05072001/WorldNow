package com.jasmeet.worldnow.screens.settingAccount.selectInterest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jasmeet.worldnow.appComponents.ButtonComponent
import com.jasmeet.worldnow.appComponents.Space
import com.jasmeet.worldnow.data.InterestList
import com.jasmeet.worldnow.data.interestList
import com.jasmeet.worldnow.ui.theme.inter

@Composable
fun InterestSelectionScreen() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        InterestSelectionLayout()

    }
}

@Composable
fun InterestSelectionLayout() {

    val selectedItemCount = rememberSaveable(key = "selectedItemCount") {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Let's get you started",
            fontSize = 25.sp,
            fontFamily = inter,
            fontWeight = FontWeight(900),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Space(height = 20.dp)

        Text(
            text = "Select Your Interests(at-least 3)",
            fontSize = 22.sp,
            fontFamily = inter,
            fontWeight = FontWeight(800),
            color = MaterialTheme.colorScheme.primary
        )
        Space(height = 16.dp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ){
            items(interestList.size){ index ->
                InterestItem(
                    interest = interestList[index],
                    selectedItemCount = selectedItemCount
                )
            }

        }

        ButtonComponent(
            onclick = {
                      //TODO: Implement the button click

            },
            text = "Continue",
            isEnabled = selectedItemCount.intValue >= 3,
        )

        Space(height =15.dp)
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun InterestItem(interest: InterestList, selectedItemCount: MutableIntState) {
    val isSelected = rememberSaveable(key = interest.interestName) {
        mutableStateOf(false)
    }

    ElevatedCard(
        modifier =Modifier
            .padding(2.dp)
            .then(
                if (isSelected.value) {
                    Modifier.background(
                        Color(0xB3000000),RoundedCornerShape(20.dp)
                    ).alpha(0.5f)
                } else {
                    Modifier
                }
            )
            .size(
                LocalConfiguration.current.screenWidthDp.dp * 0.25f,
                LocalConfiguration.current.screenWidthDp.dp * 0.35f
            )
            .clip(RoundedCornerShape(20.dp))
            .selectable(
                selected = isSelected.value,
                onClick = {
                    isSelected.value = !isSelected.value
                    if (isSelected.value) {
                        selectedItemCount.intValue ++
                    } else {
                        selectedItemCount.intValue --
                    }
                }
            ),
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            GlideImage(
                model = interest.interestImage,
                contentDescription = "Interest Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenWidthDp.dp * 0.35f),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = interest.interestName,
                fontSize = 18.sp,
                fontFamily = inter,
                fontWeight = FontWeight(800),
                color = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }


    }
}