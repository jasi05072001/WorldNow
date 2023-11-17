package com.jasmeet.worldnow.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert
    suspend fun insert(newsData: NewsData)

    @Query("SELECT * FROM SavedNews WHERE savedAt = :savedAt")
    suspend fun getNewsDataById(savedAt: Long): NewsData

    @Query("SELECT * FROM SavedNews")
    suspend fun getAllNewsData(): List<NewsData>

    @Query("DELETE FROM SavedNews WHERE savedAt = :savedAt")
    suspend fun deleteNewsDataById(savedAt: Long)

}