package com.jasmeet.worldnow.screens.home.mainScreenPage1

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun MainScreenPage1()
{
    val sliderList = remember {
        mutableListOf(
            "https://www.gstatic.com/webp/gallery/1.webp",
            "https://www.gstatic.com/webp/gallery/2.webp",
            "https://www.gstatic.com/webp/gallery/3.webp",
            "https://www.gstatic.com/webp/gallery/4.webp",
            "https://www.gstatic.com/webp/gallery/5.webp",
        )
    }
    CustomSlider(sliderList = sliderList)

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    sliderList: MutableList<String>,
) {

    val pagerState = rememberPagerState(pageCount = {sliderList.size})
    Column(
        modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
        ) {
            VerticalPager(
                state = pagerState,
                modifier = modifier.fillMaxSize()
            ) { page ->
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                val scaleFactor = 0.75f + (1f - 0.75f) * (1f - pageOffset.absoluteValue)


                Column(modifier = modifier
                    .graphicsLayer {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    .alpha(
                        scaleFactor.coerceIn(0f, 1f)
                    )

                    .clip(RoundedCornerShape(5.dp))) {

                    sliderList.forEach {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(color = Color.Cyan)) {
                            Text(
                                text = it,
                                color = Color.Black)
                        }
                    }
                }
            }

        }
    }
}