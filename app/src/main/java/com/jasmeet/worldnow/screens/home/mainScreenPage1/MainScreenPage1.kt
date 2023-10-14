package com.jasmeet.worldnow.screens.home.mainScreenPage1

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jasmeet.worldnow.R
import com.jasmeet.worldnow.data.news.Article
import com.jasmeet.worldnow.ui.theme.helventica
import com.jasmeet.worldnow.utils.getProfileImgUrlFromFirebase
import com.jasmeet.worldnow.utils.returnCountryFromSharedPrefs
import com.jasmeet.worldnow.viewModels.NewsViewModel

@Composable
fun MainScreenPage1() {
    CategoriesLayout()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesLayout(
) {
    val newsViewModel = NewsViewModel()
    val selectedInterests = returnCountryFromSharedPrefs(LocalContext.current)
    var selectedButton by remember { mutableIntStateOf(0) }
    val loading = remember { mutableStateOf(true) }

    val rememberedNews = remember {
        mutableStateOf(emptyList<Article>())
    }
    val context = LocalContext.current

    val imgUrl = remember {
        mutableStateOf("")
    }



    LaunchedEffect(
        key1 = selectedButton,
        block = {
            loading.value = true
           imgUrl.value = getProfileImgUrlFromFirebase()
            newsViewModel.getNews(
                selectedInterests[selectedButton],
                1,
                successCallBack = {
                    for (article in it?.articles.orEmpty()) {
                        article?.urlToImage?.let { img ->
                            Log.d("Launched", "CategoriesLayout: $img")
                        }
                    }
                    val newsFromApi = it!!.articles
                    rememberedNews.value = newsFromApi

                    loading.value = false
                },
                failureCallback = {
                    loading.value = true
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

                }
            )
        }
    )



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name).uppercase(),
                        style = TextStyle(
                            fontSize = 30.sp,
                            lineHeight = 22.sp,
                            fontFamily = helventica,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCFF4D2),
                            textAlign = TextAlign.Center,
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Log.d("TAGER", "CategoriesLayout:${imgUrl.value} ")
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_menu),
                            contentDescription = "Menu",
                            tint = Color(0xffCFF4D2),
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                },
                actions = {
                   AsyncImage(
                       model =imgUrl.value,
                       contentDescription =null,
                       modifier = Modifier
                           .size(40.dp)
                           .clip(CircleShape),
                       onError = {
                           Log.d("Main", "CategoriesLayout: $it")
                       }
                   )

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xff215273)
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it)
        ) {
            if (loading.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )

            }
            Column {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                    items(selectedInterests.size) { index ->
                        val buttonText = selectedInterests[index]
                        val isSelected = index == selectedButton

                        OutlinedButton(
                            onClick = {
                                selectedButton = index
                                Log.d("TAG", "CategoriesLayout: ${selectedInterests[index]}")

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
                if (!loading.value){
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(rememberedNews.value) { articles ->
                            if (!articles.urlToImage.isNullOrBlank()) {
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = articles.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = articles.description,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}



