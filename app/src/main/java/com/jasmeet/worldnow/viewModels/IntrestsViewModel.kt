package com.jasmeet.worldnow.viewModels


import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.jasmeet.worldnow.data.InterestList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(private val preferences: SharedPreferences)
    :ViewModel() {

    fun addChoices(country:String, interests:MutableList<InterestList>){
        val editor = preferences.edit()
        val gson = Gson()
        val json = gson.toJson(interests)
        editor.putString("interests", json)
        editor.putString("country", country)

        val success = editor.commit()
        if (!success) {
            throw RuntimeException("Failed to save choices")
        }
    }

    fun getChoices(): Pair<String?, MutableList<InterestList>> {
        val json = preferences.getString("interests", null)
        val country = preferences.getString("country", null)

        val gson = Gson()
        val interestsType = object : TypeToken<MutableList<InterestList>>() {}.type
        val interests: MutableList<InterestList> = gson.fromJson(json, interestsType) ?: mutableListOf()

        return Pair(country, interests)
    }

}


