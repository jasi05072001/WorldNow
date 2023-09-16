package com.jasmeet.worldnow.viewModels


import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(private val preferences: SharedPreferences)
    :ViewModel() {



    fun addChoices(country:String, interests:List<String>){

        val editor = preferences.edit()
        editor.putString("country", country)
        editor.putStringSet("interests", interests.toSet())
        val success = editor.commit()
        if (!success) {
            throw RuntimeException("Failed to save choices")
        }
    }

}


