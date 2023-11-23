package com.jasmeet.worldnow.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedNews")
data class NewsData(

    @ColumnInfo(name = "title")
    val title :String,

    @ColumnInfo(name = "description")
    val description :String,

    @PrimaryKey
    @ColumnInfo(name = "newsUrl")
    val newsUrl :String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl :String,

    @ColumnInfo(name = "savedAt")
    val savedAt :Long

)
