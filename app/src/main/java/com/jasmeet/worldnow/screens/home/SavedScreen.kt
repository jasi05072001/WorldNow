package com.jasmeet.worldnow.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jasmeet.worldnow.utils.convertTimestampToDate
import com.jasmeet.worldnow.viewModels.NewsViewModel

@Composable
fun SavedScreen() {
    val newsViewModel: NewsViewModel = hiltViewModel()

    val savedNews = newsViewModel.newsList.observeAsState(initial = emptyList())

    LaunchedEffect(
        key1 = true,
        block ={
            newsViewModel.getAllNewsData()
        }
    )


    if (savedNews.value.isEmpty()){
        Text(text = "No Saved News", modifier = Modifier.fillMaxSize())
    }
    else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(savedNews.value) {
                val convertMillisToDate = convertTimestampToDate(it.savedAt)
                Text(text = convertMillisToDate)

            }
        }
    }


}