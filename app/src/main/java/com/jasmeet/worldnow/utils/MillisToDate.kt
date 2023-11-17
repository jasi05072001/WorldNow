package com.jasmeet.worldnow.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertTimestampToDate(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        "error"
    }
}