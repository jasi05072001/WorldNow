package com.jasmeet.worldnow.api

import com.jasmeet.worldnow.data.news.News
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "https://newsapi.org/v2/"
const val API_KEY = "11bbedc333bc43c9bf77d18ef103c10d"

interface NewsApi {

    @GET("everything?apiKey=$API_KEY&lang=en")
    fun getHeadLines(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ):Call<News>
}
class NewsWebService {
    private var api :NewsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(NewsApi::class.java)
    }

    fun getNews(q: String, page: Int,pageSize: Int): Call<News> {
        return api.getHeadLines(q, page,pageSize)
    }
}