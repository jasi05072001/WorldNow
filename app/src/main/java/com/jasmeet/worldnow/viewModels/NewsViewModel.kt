package com.jasmeet.worldnow.viewModels

import androidx.lifecycle.ViewModel
import com.jasmeet.worldnow.data.news.News
import com.jasmeet.worldnow.repository.NewsRepository

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
):ViewModel() {

    fun getNews(
        query :String,
        page :Int,
        successCallBack :(response : News?) ->Unit,
        failureCallback: (error: String) -> Unit,
        pagSize :Int = 70
    ){
        repository.getNews(query,page,successCallBack,failureCallback,pagSize)
    }
}