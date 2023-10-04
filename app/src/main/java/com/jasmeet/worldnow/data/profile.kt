package com.jasmeet.worldnow.data

data class Profile(
    val name:String,
    val userName:String,
    val email:String,
    val country:String,
    val interest:List<String>,
    val photoUrl:String
)
