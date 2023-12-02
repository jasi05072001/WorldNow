package com.jasmeet.worldnow.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import androidx.core.app.NotificationCompat
import com.jasmeet.worldnow.R
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class SendNotification(context: Context) : AsyncTask<String, Void, Bitmap>() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private var message: String? = null

    override fun doInBackground(vararg params: String): Bitmap? {
        val message = params[0]
        this.message = message

        return try {
            val url = URL(params[2])
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        val context = contextRef.get()

        if (context != null) {
            try {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val notificationId = System.currentTimeMillis().toInt()
                val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(message)
                    .setSmallIcon(R.drawable.ic_bookmark_unselected)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(result)
                    )
                    .setPriority(NotificationCompat.FLAG_BUBBLE)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel =
                        NotificationChannel(
                            CHANNEL_ID,
                            "Channel Name",
                            NotificationManager.IMPORTANCE_HIGH
                        )
                    notificationManager.createNotificationChannel(channel)
                    notificationBuilder.setChannelId(channel.id)
                }

                val notification = notificationBuilder.build()
                notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

                notificationManager.notify(notificationId, notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    companion object {
        private const val CHANNEL_ID = "channel_id"
    }
}
