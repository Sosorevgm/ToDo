package com.sosorevgm.todo.domain.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.sosorevgm.todo.R
import com.sosorevgm.todo.features.main.MainActivity
import javax.inject.Inject

class PushManager @Inject constructor(
    private val context: Context
) {

    companion object {
        private const val DEFAULT_CHANNEL_ID = "default.channel.id"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(DEFAULT_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(tasksCount: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationText = StringBuilder()
        notificationText.append(context.getString(R.string.notification_text_start))
        notificationText.append(" ")
        notificationText.append(tasksCount)
        notificationText.append(" ")
        notificationText.append(context.getString(R.string.notification_text_end))

        val builder = NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_notification)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(notificationText.toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(tasksCount, builder.build())
    }
}