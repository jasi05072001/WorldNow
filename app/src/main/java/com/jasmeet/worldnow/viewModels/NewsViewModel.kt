package com.jasmeet.worldnow.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jasmeet.worldnow.data.news.News
import com.jasmeet.worldnow.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
):ViewModel() {

    private var currentPage = 1

   var isEndReach = mutableStateOf(false)

    fun getNews(
        query :String,
        page :Int,
        successCallBack :(response : News?) ->Unit,
        failureCallback: (error: String) -> Unit,
        pagSize :Int = 50
    ){
        repository.getNews(query,page,successCallBack,failureCallback,pagSize)
    }
}