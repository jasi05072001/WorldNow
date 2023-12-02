package com.jasmeet.worldnow.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve title and imageURL from the intent
        val title = intent.getStringExtra("title")
        val imageURL = intent.getStringExtra("imageURL")

        // Handle the notification creation here
        SendNotification(context).execute(title, "Content", imageURL)
    }
}
