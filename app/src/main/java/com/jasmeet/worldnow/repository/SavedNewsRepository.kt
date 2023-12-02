package com.jasmeet.worldnow.repository

import com.jasmeet.worldnow.room.NewsDao
import com.jasmeet.worldnow.room.NewsData
import javax.inject.Inject

class SavedNewsRepository  @Inject constructor(
    private val newsDao :NewsDao
){
    suspend fun insert(newsData: NewsData){
        newsDao.insert(newsData)
    }

    suspend fun getNewsDataById(savedAt: Long): NewsData{
        return newsDao.getNewsDataById(savedAt)
    }

    suspend fun getAllNewsData(): List<NewsData>{
        return newsDao.getAllNewsData()
    }

    suspend fun deleteNewsDataById(url: String){
        newsDao.deleteNewsDataById(url)
    }

}