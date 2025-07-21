package soy.gabimoreno.framework.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import soy.gabimoreno.R
import soy.gabimoreno.presentation.MainActivity

fun buildNotification(context: Context) {
    val intent =
        Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

    val channelId = context.getString(R.string.new_premium_audio_channel_id)
    val builder =
        NotificationCompat
            .Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("textTitle")
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}

const val NOTIFICATION_REQUEST_CODE = 0
const val NOTIFICATION_ID = 1001

const val TOPIC_NEW_AUDIO = "newAudio"
