package soy.gabimoreno.framework.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import androidx.core.content.res.ResourcesCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import soy.gabimoreno.MainActivity
import soy.gabimoreno.R
import soy.gabimoreno.domain.Audio
import soy.gabimoreno.player.Player
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val NOTIFICATION_ID = 1

        private const val EXTRA_AUDIO = "EXTRA_AUDIO"

        fun start(
            context: Context,
            audio: Audio
        ) {
            val intent = Intent(context, PlayerService::class.java).apply {
                putExtra(EXTRA_AUDIO, audio)
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, PlayerService::class.java))
        }
    }

    private var audioName: String? = null
    private var audioDescription: String? = null
    private var audioThumbnailUrl: String? = null

    @Inject
    lateinit var player: Player
    private val binder: MediaServiceBinder by lazy { MediaServiceBinder() }
    private lateinit var notificationManager: PlayerNotificationManager

    private val mediaDescriptionAdapter =
        object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: com.google.android.exoplayer2.Player): CharSequence {
                return audioName ?: "Default title"
            }

            override fun getCurrentContentText(player: com.google.android.exoplayer2.Player): CharSequence? {
                return audioDescription
            }

            override fun getCurrentLargeIcon(
                player: com.google.android.exoplayer2.Player,
                bitmapCallback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                // TODO: Implement the `toBitmap` method
//                audioThumbnailUrl.toBitmap(applicationContext) { bitmap ->
//                    bitmapCallback.onBitmap(bitmap)
//                }
                return null
            }

            override fun createCurrentContentIntent(player: com.google.android.exoplayer2.Player): PendingIntent? {
                val intent = Intent(applicationContext, MainActivity::class.java)
                return PendingIntent.getActivity(applicationContext, 0, intent, 0)
            }
        }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        val song = intent?.getSerializableExtra(EXTRA_AUDIO) as? Audio
        song?.let {
            audioName = song.name
            audioDescription = song.description
            audioThumbnailUrl = song.thumbnailUrl
        }

        buildNotification()

        return START_STICKY
    }

    private fun buildNotification() {
        notificationManager = PlayerNotificationManager.Builder(
            this,
            NOTIFICATION_ID,
            CHANNEL_ID
        )
            .setChannelNameResourceId(R.string.app_name)
            .setChannelDescriptionResourceId(ResourcesCompat.ID_NULL)
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setNotificationListener(
                object : PlayerNotificationManager.NotificationListener {
                    override fun onNotificationPosted(
                        notificationId: Int,
                        notification: Notification,
                        ongoing: Boolean
                    ) {
                        startForeground(
                            notificationId,
                            notification
                        )
                    }

                    override fun onNotificationCancelled(
                        notificationId: Int,
                        dismissedByUser: Boolean
                    ) {
                        stopSelf()
                    }
                }
            ).build()

        // TODO: Check how to manage this
        notificationManager.setUseNextAction(false)
//        notificationManager.setFastForwardIncrementMs(0)
//        notificationManager.setRewindIncrementMs(0)
        notificationManager.setUseStopAction(true)
        notificationManager.setUseChronometer(true)

        // TODO: Create this icon how to manage this
        notificationManager.setSmallIcon(R.drawable.ic_stat_name)

        notificationManager.setPlayer(player.getExoPlayer())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::notificationManager.isInitialized) {
            notificationManager.setPlayer(null)
        }
    }

    inner class MediaServiceBinder : Binder() {
        val service: PlayerService get() = this@PlayerService
    }
}
