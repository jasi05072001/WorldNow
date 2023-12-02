package com.jasmeet.worldnow

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.jasmeet.worldnow.dataStore.DataStoreUtil
import com.jasmeet.worldnow.notifications.NotificationReceiver
import com.jasmeet.worldnow.screens.mainScreen.MainApp
import com.jasmeet.worldnow.ui.theme.WorldNowTheme
import com.jasmeet.worldnow.viewModels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var dataStoreUtil: DataStoreUtil
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        dataStoreUtil = DataStoreUtil(applicationContext)

        var title = ""
        var imgUrl = ""

        val newsViewModel : NewsViewModel by viewModels()
        val currentDate = java.time.LocalDate.now().minusDays(4).toString()
        val systemTheme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> { true }
            Configuration.UI_MODE_NIGHT_NO -> { false }
            else -> { false }
        }
        super.onCreate(savedInstanceState)

        newsViewModel.getRandomNews(
            "trending",
            1,
            successCallBack = {
                if (it != null && it.articles.isNotEmpty()) {
                    val randomIndex = Random.nextInt(0, it.articles.size)
                    title = it.articles[randomIndex].title
                    imgUrl = it.articles[randomIndex].urlToImage
                    Log.d("TAG", "onCreate: $title $imgUrl")
                    scheduleRepeatingNotification(title, imgUrl, applicationContext)
                }
            },

            failureCallback = {},
            from = currentDate
        )


        setContent {
            val theme = dataStoreUtil.getTheme(systemTheme).collectAsState(initial = systemTheme)

            WorldNowTheme(theme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MainApp(dataStoreUtil,theme.value)
                }
            }
        }
    }
}

 fun scheduleRepeatingNotification(title: String, imageURL: String,context: Context) {
    val alarmManager =context.getSystemService(ALARM_SERVICE) as AlarmManager
    val notificationIntent = Intent(context, NotificationReceiver::class.java)
    notificationIntent.putExtra("title", title)
    notificationIntent.putExtra("imageURL", imageURL)

    val pendingIntent =
        PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

    // Set the alarm to start at a specific time (you can modify this as needed)
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 12) // Set the hour
    calendar.set(Calendar.MINUTE, 0) // Set the minute

    // Set the alarm to repeat every 4 hours
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        30 * 1000, // 4 hours in milliseconds
        pendingIntent
    )
}




