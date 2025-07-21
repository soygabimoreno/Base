package soy.gabimoreno.framework.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import soy.gabimoreno.R

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.new_premium_audio_channel_name)
        val description = context.getString(R.string.new_premium_audio_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channelId = context.getString(R.string.new_premium_audio_channel_id)
        val channel =
            NotificationChannel(channelId, name, importance).apply {
                this.description = description
            }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
