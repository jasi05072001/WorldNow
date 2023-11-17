package com.jasmeet.worldnow.repository

import com.jasmeet.worldnow.api.NewsWebService
import com.jasmeet.worldnow.data.news.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val webService: NewsWebService
)  {

    fun getNews(
        query :String,
        page :Int,
        successCallBack :(response :News?) ->Unit,
        failureCallback: (error: String) -> Unit,
        pageSize :Int = 70,
        from :String
    ){

        val result =  webService.getNews(query,page,pageSize,from).enqueue(object:Callback<News>{
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