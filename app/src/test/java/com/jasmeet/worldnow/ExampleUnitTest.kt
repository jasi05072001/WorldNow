package com.jasmeet.worldnow

import com.jasmeet.worldnow.utils.convertTimestampToMonthAndYear
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class YourTestClass {

    @Test
    fun testConvertTimestampToMonthAndYear() {
        val timestamp = SimpleDateFormat("yyyy-MM-dd").parse("2023-10-15").time

        val result = convertTimestampToMonthAndYear(timestamp)

        val expected = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date(timestamp))

        assertEquals(expected, result)
    }
}
