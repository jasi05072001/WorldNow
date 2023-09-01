@file:OptIn(ExperimentalFoundationApi::class)

package com.jasmeet.worldnow.screens.introScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.ui.theme.inter
import kotlinx.coroutines.launch


@Composable
fun IntroScreen() {
    val coroutineScope = rememberCoroutineScope()

    val imgList = listOf(
        R.drawable.onboarding_page_1,
        R.drawable.onboarding_page_2,
        R.drawable.onboarding_page_3,
    )
    val pagerState = rememberPagerState(pageCount = {
        imgList.size
    })
    val pageCount = imgList.size


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFececec)),
        contentAlignment = Alignment.BottomCenter

    ) {
        HorizontalPager(
            state =pagerState,
            modifier = Modifier
                .fillMaxSize(),
            userScrollEnabled = false

        ) { page ->

            Image(
                painter = painterResource(id = imgList[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
        val buttonText = if (pagerState.currentPage == 2) "Start" else "Continue"

        Column(
            modifier = Modifier.padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val text = when (pagerState.currentPage) {
                0 -> {
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontFamily = inter,
                            )
                        ) {
                            append("News according \n to your \n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFb1eebc),
                                fontFamily = inter,
                                letterSpacing = 1.sp,
                            )
                        ) {
                            append("Preference and \n Interest")
                        }
                    }
                }
                1 -> {
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontFamily = inter,
                            )
                        ) {
                            append("Read anytime \n anywhere even \n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFb1eebc),
                                fontFamily = inter,
                                letterSpacing = 1.sp,
                            )
                        ) {
                            append(" without internet")
                        }
                    }
                }
                else -> {
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontFamily = inter,
                            )
                        ) {
                            append("Share with your \n friends \n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFb1eebc),
                                fontFamily = inter,
                                letterSpacing = 1.sp,
                            )
                        ) {
                            append("and community")
                        }
                    }
                }
            }

            Text(
                text = text,
                modifier = Modifier,
                color = Color(0xFF32989b),
                fontSize = 40.sp,
                fontFamily = inter,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )


            Row(
                Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color(0xff4499ac) else Color.White
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .width(40.dp)
                            .height(8.dp)
                            .background(color)
                            .size(20.dp)

                    )
                }
            }

            Spacer(modifier = Modifier.height( 16.dp))

            Button(
                onClick = {
                    val nextPage = if (pagerState.currentPage < 2) pagerState.currentPage + 1 else 2
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = nextPage,
                            animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = 0,
                                easing = LinearEasing
                            )
                        )
                    }
                    if (pagerState.currentPage == 2) AppRouter.navigateTo(Screens.SignInScreen)

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xffcef4d2)
                ),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = buttonText,
                    color = Color(0xFF215273),
                    fontSize = 20.sp,
                    fontFamily = inter,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontWeight = FontWeight.W500
                )
            }
            val textBtn = if (pagerState.currentPage == 2) "" else "Skip"
            TextButton(
                onClick = { AppRouter.navigateTo(Screens.SignUpScreen) },
                modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = textBtn,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontFamily = inter,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W500,
                    textDecoration = TextDecoration.Underline
                )
            }

        }
    }
}