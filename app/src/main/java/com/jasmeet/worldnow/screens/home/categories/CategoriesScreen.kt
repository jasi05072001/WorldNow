package com.jasmeet.worldnow.screens.home.categories

import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.appComponents.SearchFieldComponent
import com.jasmeet.worldnow.data.news.Article
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.room.NewsData
import com.jasmeet.worldnow.ui.theme.darkButtonColor
import com.jasmeet.worldnow.ui.theme.helventica
import com.jasmeet.worldnow.utils.getProfileImgAndName
import com.jasmeet.worldnow.utils.getSelectedInterests
import com.jasmeet.worldnow.utils.removeBrackets
import com.jasmeet.worldnow.utils.removeWhitespaces
import com.jasmeet.worldnow.viewModels.NewsViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesView() {

    val newsViewModel :NewsViewModel = hiltViewModel()
    val context = LocalContext.current
    val interests = rememberSaveable { mutableStateOf(listOf<String>()) }

    val rememberedCategoriesNews = rememberSaveable {
        mutableStateOf(emptyList<Article>())
    }
    val selectedButton = remember { mutableIntStateOf(0) }
    val currentDate = java.time.LocalDate.now().minusDays(5).toString()

    val loading = rememberSaveable { mutableStateOf(true) }

    val namePhotoPair = remember {
        mutableStateOf(Pair("", ""))
    }
    val newsSearched = remember {
        mutableStateOf("")
    }
    val isSearchClicked = rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(
        key1 = interests.value,
        block = {
            loading.value = true
            interests.value = getSelectedInterests()
            namePhotoPair.value= getProfileImgAndName()
            newsViewModel.getNews(
                interests.value[0],
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
        }

    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedVisibility (
                        visible = !isSearchClicked.value,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name).uppercase(),
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontFamily = helventica,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCFF4D2),
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                    AnimatedVisibility(
                        visible = isSearchClicked.value,
                        enter = fadeIn() ,
                        exit = fadeOut() + shrinkVertically()
                    ){
                        SearchFieldComponent(
                            value = newsSearched.value,
                            labelValue = "Enter your query",
                            onValueChange ={
                                newsSearched.value = it
                            },
                            isIconVisible = false,
                            modifier = Modifier
                                .height(48.dp)
                                .width(LocalConfiguration.current.screenWidthDp.dp * 0.8f),
                            fontsize = 12.sp
                        )
                    }
                },

                navigationIcon = {
                    AnimatedVisibility(
                        visible = !isSearchClicked.value,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        IconButton(
                            onClick = {
                                AppRouter.navigateTo(Screens.HomeScreen)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Menu",
                                tint = Color(0xffCFF4D2),
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xff215273)
                ),
                actions = {
                    AnimatedVisibility(
                        visible = !isSearchClicked.value,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        IconButton(
                            onClick = {
                                isSearchClicked.value = !isSearchClicked.value

                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.search),
                                contentDescription = "Search",
                                tint = Color(0xffCFF4D2),
                            )
                        }

                    }
                    AnimatedVisibility(
                        visible = newsSearched.value.isNotEmpty() || isSearchClicked.value,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        IconButton(
                            onClick = {
                                isSearchClicked.value = !isSearchClicked.value
                                newsSearched.value = ""

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Search",
                                tint = Color(0xffCFF4D2),
                            )
                        }

                    }

                }

            )
        },
    ){paddingValues ->

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
                                    NewsItemLayout(articles,newsViewModel = newsViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}

@Composable
fun NewsItemLayout(
    articles: Article,
    newsViewModel: NewsViewModel
) {

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
                        onClick = {
                            newsViewModel.insertNews(
                                newsData = NewsData(
                                    title =  articles.title,
                                    description = articles.description,
                                    newsUrl = articles.url,
                                    imageUrl =  if (articles.urlToImage.isNullOrEmpty()) "https://demofree.sirv.com/nope-not-here.jpg" else removeWhitespaces(articles.urlToImage),
                                    savedAt = System.currentTimeMillis()

                                )
                            )
                            Toast.makeText(context, "Article Saved", Toast.LENGTH_SHORT).show()
                        }
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





