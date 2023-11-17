package com.jasmeet.worldnow.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsData::class], version = 1, exportSchema = false)
abstract class NewsDataBase :RoomDatabase(){
        abstract fun getNewsDao(): NewsDao
}