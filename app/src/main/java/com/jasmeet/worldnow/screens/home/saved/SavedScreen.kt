package com.jasmeet.worldnow.screens.home.saved

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.jasmeet.worldnow.navigation.AppRouter
import com.jasmeet.worldnow.navigation.Screens
import com.jasmeet.worldnow.navigation.SystemBackButtonHandler
import com.jasmeet.worldnow.room.NewsData
import com.jasmeet.worldnow.ui.theme.helventica
import com.jasmeet.worldnow.utils.convertTimestampToMonthAndYear
import com.jasmeet.worldnow.utils.removeBrackets
import com.jasmeet.worldnow.utils.removeWhitespaces
import com.jasmeet.worldnow.viewModels.NewsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen() {
    val newsViewModel: NewsViewModel = hiltViewModel()

    val savedNews = newsViewModel.newsList.observeAsState(initial = emptyList())

    val isExpandedMap = rememberSaveable { mutableMapOf<String, MutableState<Boolean>>() }


    LaunchedEffect(
        key1 = savedNews.value,
        block ={
            newsViewModel.getAllNewsData()
        }
    )


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
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
                },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            AppRouter.navigateTo(Screens.HomeScreen)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xffCFF4D2),
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xff215273)
                ),
            )
        },
    ) {padding ->
        if (savedNews.value.isEmpty()) {
            Text(text = "No Saved News", modifier = Modifier
                .fillMaxSize()
                .padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = 10.dp)
            ) {
                val groupedNews =
                    savedNews.value.groupBy { convertTimestampToMonthAndYear(it.savedAt) }


                groupedNews.forEach { (date, newsList) ->
                    isExpandedMap.getOrPut(date) { mutableStateOf(true) }
                    item {
                        // saved date as a header
                        OutlinedCard(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                isExpandedMap[date]?.value = !(isExpandedMap[date]?.value ?: true)
                            }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = date,
                                    modifier = Modifier
                                        .padding(top = 8.dp, bottom = 8.dp, start = 7.dp, end = 5.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Icon(
                                    imageVector = if (isExpandedMap[date]?.value == true) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                    contentDescription = "Expand",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }

                    // list of news for the current date
                    items(newsList) { newsItem ->
                        AnimatedVisibility(
                            visible = isExpandedMap[date]?.value ?: true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut()+ shrinkVertically()
                        ) {
                            SavedNewsItemLayout(
                                articles = newsItem,
                                newsViewModel = newsViewModel
                            )
                        }
                    }



                }
            }
        }
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screens.HomeScreen)
    }


}

@Composable
fun SavedNewsItemLayout(
    articles: NewsData,
    newsViewModel: NewsViewModel,
) {

    val context = LocalContext.current
    val imgUrl = if (articles.imageUrl.isNullOrEmpty()) "https://demofree.sirv.com/nope-not-here.jpg" else removeWhitespaces(articles.imageUrl)
    val displayImg = removeBrackets(imgUrl)
    val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp / 8)
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .clickable {
                AppRouter.savedDetailedArticles = NewsData(
                    title = articles.title,
                    description = articles.description,
                    imageUrl = articles.imageUrl,
                    newsUrl = articles.newsUrl,
                    savedAt = articles.savedAt,
                )
                AppRouter.navigateTo(
                    Screens.SavedDetailsArticlesScreen
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
                            val url = articles.newsUrl
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
                            newsViewModel.deleteNewsDataById(articles.savedAt)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint =  MaterialTheme.colorScheme.onSurface
                        )
                    }

                }
            }

        }
    }
}

fun convertDateAndTimeToMillis(dateTimeString: String): Long {
    val format = SimpleDateFormat("dd MMMM hh a", Locale.getDefault())
    val date = format.parse(dateTimeString)

    return date?.time ?: 0L
}

fun main() {
    val dateTimeString = "18 October 3 PM"
    val milliseconds = convertDateAndTimeToMillis(dateTimeString)
    println("Milliseconds since epoch: $milliseconds")
}
