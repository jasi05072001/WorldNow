package com.jasmeet.worldnow.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertTimestampToMonthAndYear(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        "error"
    }
}

