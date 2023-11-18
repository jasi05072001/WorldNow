package com.jasmeet.worldnow.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jasmeet.worldnow.WorldNowApplication
import com.jasmeet.worldnow.api.NewsWebService
import com.jasmeet.worldnow.repository.NewsRepository
import com.jasmeet.worldnow.repository.SavedNewsRepository
import com.jasmeet.worldnow.room.NewsDao
import com.jasmeet.worldnow.room.NewsDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesFirebaseFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("choices", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesNewsDatabase(@ApplicationContext context: Context): NewsDataBase {
        return Room.databaseBuilder(
            context,
            NewsDataBase::class.java,
            "NewsDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun providesNewsDao(newsDataBase: NewsDataBase) :NewsDao{
        return newsDataBase.getNewsDao()
    }

    @Provides
    @Singleton
    fun providesNewsRepository(newsDao: NewsDao) : SavedNewsRepository {
        return SavedNewsRepository(newsDao)
    }

    @Provides
    @Singleton
    fun provideNewsWebService(): NewsWebService {
        return NewsWebService()
    }


    @Provides
    @Singleton
    fun provideNewsRepository(webService: NewsWebService): NewsRepository {
        return NewsRepository(webService)
    }




}