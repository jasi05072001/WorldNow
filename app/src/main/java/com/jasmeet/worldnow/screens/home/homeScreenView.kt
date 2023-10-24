package com.jasmeet.worldnow.screens.home

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jasmeet.worldnow.appComponents.bounceClick
import com.jasmeet.worldnow.data.news.Article
import com.jasmeet.worldnow.ui.theme.darkButtonColor
import kotlin.math.absoluteValue



@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
fun HomeScreenMainView(
    loading: MutableState<Boolean>,
    paddingValues: PaddingValues,
    pagerState: PagerState,
    rememberedNews: MutableState<List<Article>>,
    context: Context,
    isDark: Boolean
) {



    if (!loading.value) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    if (page in 0 until rememberedNews.value.size) {


                        val pageOffset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                        val scaleFactor =
                            0.75f + (1f - 0.75f) * (1f - pageOffset.absoluteValue)

                        val article = rememberedNews.value[page]

                        Column(modifier = Modifier
                            .padding(10.dp)
                            .graphicsLayer {
                                scaleX = scaleFactor
                                scaleY = scaleFactor
                            }
                            .alpha(
                                scaleFactor.coerceIn(0f, 1f)
                            )

                            .clip(RoundedCornerShape(15.dp))) {
                            Column(
                                Modifier
                                    .shadow(
                                        10.dp,
                                        RoundedCornerShape(15.dp),
                                        ambientColor = Color.Yellow
                                    )
                                    .fillMaxSize()
                                    .background(color = MaterialTheme.colorScheme.primary)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(article.urlToImage)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(LocalConfiguration.current.screenHeightDp.dp / 3.5f)
                                        .clip(
                                            RoundedCornerShape(
                                                topEnd = 15.dp,
                                                topStart = 15.dp
                                            )
                                        )
                                )

                                Text(
                                    text = article.title,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(10.dp),

                                    )
                                Text(
                                    text = article.description,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(10.dp),
                                )
                                Spacer(modifier = Modifier.weight(1f))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    Button(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                        onClick = {
                                            openTab(
                                                context,
                                                article.url,
                                                isDark
                                            )
                                        },
                                        modifier = Modifier
                                            .bounceClick()
                                            .align(Alignment.BottomEnd),
                                    ) {
                                        Text(
                                            text = "Read More",
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                }
                            }

                        }
                    }
                }

            }
        }

    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center),
                trackColor = Color.Gray,
                color = darkButtonColor,
                strokeCap = StrokeCap.Butt,
                strokeWidth = 5.dp
            )
        }
    }
}