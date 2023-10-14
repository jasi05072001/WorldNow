package com.jasmeet.worldnow.repository

import com.jasmeet.worldnow.api.NewsWebService
import com.jasmeet.worldnow.data.news.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository(
    private val webService :NewsWebService = NewsWebService()
) {

    fun getNews(
        query :String,
        page :Int,
        successCallBack :(response :News?) ->Unit,
        failureCallback: (error: String) -> Unit,
        pageSize :Int = 50
    ){

        val result =  webService.getNews(query,page,pageSize).enqueue(object:Callback<News>{
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful){
                    successCallBack(response.body())
                }
                else {
                    failureCallback("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                failureCallback(t.message ?: "Unknown error")
            }
        }
        )
        return result
    }
}