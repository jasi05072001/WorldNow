package com.jasmeet.worldnow.screens.home.categories

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jasmeet.worldnow.data.news.Article
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.ui.theme.darkButtonColor
import com.jasmeet.worldnow.ui.theme.helventica
import com.jasmeet.worldnow.utils.removeBrackets
import com.jasmeet.worldnow.utils.removeWhitespaces
import com.jasmeet.worldnow.viewModels.NewsViewModel
import kotlinx.coroutines.launch

@Composable
fun CategoriesView(
    paddingValues: PaddingValues,
    loading: MutableState<Boolean>,
    interests: MutableState<List<String>>,
    selectedButton: MutableIntState,
    newsViewModel: NewsViewModel,
    rememberedCategoriesNews: MutableState<List<Article>>,
    context: Context,
    currentDate: String,
    newsSearched: MutableState<String>
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
    ) {
        if (loading.value) {
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
        Column(modifier = Modifier.fillMaxSize()) {
            LazyRow(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(interests.value.size) { index ->
                    val buttonText = interests.value[index]
                    val isSelected = selectedButton.intValue == index

                    OutlinedButton(
                        onClick = {
                            selectedButton.intValue = index
                            newsViewModel.getNews(
                                interests.value[selectedButton.intValue],
                                1,
                                successCallBack = {
                                    val newsFromApi = it!!.articles
                                    rememberedCategoriesNews.value = newsFromApi
                                    loading.value = false
                                    Log.d("NewsButton", "HomeScreenLayout: $newsFromApi")

                                },
                                failureCallback = {
                                    loading.value = false
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

                                },
                                from = currentDate,
                            )

                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = buttonText,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        )

                    }
                }
            }

            if (!loading.value) {
                val filteredArticles = rememberedCategoriesNews.value.filter { articles ->
                    articles.urlToImage != null && articles.title != null
                            && articles.title.contains(newsSearched.value, ignoreCase = true)
                }
                if (filteredArticles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Text(
                            text = "No articles found",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.Black,
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                } else {
                    LazyColumn(

                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .height(LocalConfiguration.current.screenHeightDp.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        if (filteredArticles.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.White)
                                ) {

                                }
                            }
                        } else {
                            items(filteredArticles) { articles ->
                                NewsItemLayout(articles)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItemLayout(articles: Article) {
    val context = LocalContext.current
    val imgUrl = if (articles.urlToImage.isNullOrEmpty()) "https://demofree.sirv.com/nope-not-here.jpg" else removeWhitespaces(articles.urlToImage)
    val displayImg = removeBrackets(imgUrl)
    val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp / 8)
            .padding(horizontal = 15.dp)
            .clickable {
                AppRouter.detailedArticles = articles
                AppRouter.navigateTo(
                    Screens.DetailedScreen
                )

            },
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 8.dp,

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = if (isSystemInDarkTheme()) listOf(
                            Color(0xff215273),
                            Color(0xff359D9E)

                        ) else listOf(
                            Color(0xff7CE495),
                            Color(0xffCFF4D2)
                        )
                    )
                )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(displayImg)
                    .crossfade(true)
                    .build(),
                contentDescription =null,
                modifier = Modifier
                    .size(
                        LocalConfiguration.current.screenWidthDp.dp / 3,
                        LocalConfiguration.current.screenHeightDp.dp / 8),
                contentScale = ContentScale.FillBounds,
            )
            Column(modifier = Modifier.fillMaxSize()) {

                Text(
                    text = articles.title ,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = helventica
                    ),
                    maxLines = 2,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                )
                Spacer(modifier =Modifier.weight(1f))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    IconButton(
                        onClick = {
                            val url = articles.url
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, url)
                                type = "text/plain"
                            }
                            val chooser = Intent.createChooser(shareIntent, "Share Article")
                            shareLauncher.launch(chooser)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint =  MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "Book Mark" ,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

        }
    }
}




