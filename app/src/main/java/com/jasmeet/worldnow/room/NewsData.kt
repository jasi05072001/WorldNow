package com.jasmeet.worldnow.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedNews")
data class NewsData(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0,

    @ColumnInfo(name = "title")
    val title :String,

    @ColumnInfo(name = "description")
    val description :String,

    @ColumnInfo(name = "newsUrl")
    val newsUrl :String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl :String,

    @ColumnInfo(name = "savedAt")
    val savedAt :Long

)
