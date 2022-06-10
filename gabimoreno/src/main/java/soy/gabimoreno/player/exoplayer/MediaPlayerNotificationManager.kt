package soy.gabimoreno.player.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import soy.gabimoreno.R

class MediaPlayerNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newSongCallback: () -> Unit
) {

    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager =
            createNotificationManger(mediaController, sessionToken, notificationListener)
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    private fun createNotificationManger(
        mediaController: MediaControllerCompat,
        sessionToken: MediaSessionCompat.Token,
        notificationListener: PlayerNotificationManager.NotificationListener
    ): PlayerNotificationManager {
        return PlayerNotificationManager.Builder(
            context,
            PLAYBACK_NOTIFICATION_ID,
            PLAYBACK_NOTIFICATION_CHANNEL_ID,
            DescriptionAdapter(mediaController)
        )
            .setNotificationListener(notificationListener)
            .setChannelNameResourceId(R.string.playback_notification_channel_name)
            .setChannelDescriptionResourceId(R.string.playback_notification_channel_description)
            .build()
            .apply {
                setSmallIcon(R.drawable.ic_stat_name)
                setMediaSessionToken(sessionToken)
                setUseStopAction(true)
                setUseNextActionInCompactView(true)
                setUsePreviousActionInCompactView(true)
                setControlDispatcher(DefaultControlDispatcher(0L, 0L))
            }

        // TODO: Remove
//        return PlayerNotificationManager.createWithNotificationChannel(
//            context,
//            PLAYBACK_NOTIFICATION_CHANNEL_ID,
//            R.string.playback_notification_channel_name,
//            R.string.playback_notification_channel_description,
//            PLAYBACK_NOTIFICATION_ID,
//            DescriptionAdapter(mediaController),
//            notificationListener
//        ).apply {
//            setSmallIcon(R.drawable.ic_stat_name)
//            setMediaSessionToken(sessionToken)
//            setUseStopAction(true)
//            setUseNextActionInCompactView(true)
//            setUsePreviousActionInCompactView(true)
//            setControlDispatcher(DefaultControlDispatcher(0L, 0L))
//        }
    }

    private inner class DescriptionAdapter(
        private val mediaController: MediaControllerCompat
    ) : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentContentTitle(player: Player): CharSequence {
            newSongCallback()
            return mediaController.metadata.description.title.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Glide.with(context).asBitmap()
                .load(mediaController.metadata.description.iconUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) = Unit

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        callback.onBitmap(resource)
                    }
                })
            return null
        }
    }
}

private const val PLAYBACK_NOTIFICATION_CHANNEL_ID = "UEBWUJBQ0tfTk9USUZJQ0FUSU9OX0NIQU5ORUxfSUQ" // TODO: Change it
const val PLAYBACK_NOTIFICATION_ID = 115234045 // TODO: Change it
