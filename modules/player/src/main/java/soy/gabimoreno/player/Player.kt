package soy.gabimoreno.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.StyledPlayerView

class Player(
    private val context: Context,
) {
    private var exoPlayer: SimpleExoPlayer? = null
    private var playing = false
    private lateinit var onStop: () -> Unit

    fun getExoPlayer(): SimpleExoPlayer? = exoPlayer

    fun init(
        exoplayerView: StyledPlayerView,
        uriStrings: List<String>,
        onStop: () -> Unit,
    ) {
        this.onStop = onStop
        val trackSelector = DefaultTrackSelector(context)
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer =
            SimpleExoPlayer
                .Builder(context)
                .setTrackSelector(trackSelector)
                .build()
        exoplayerView.player = exoPlayer

        uriStrings.forEach {
            exoPlayer?.addMediaItem(MediaItem.fromUri(Uri.parse(it)))
        }
        exoPlayer?.prepare()

        val componentName = ComponentName(context, "Exo")
        val mediaSession =
            MediaSessionCompat(
                context,
                "ExoPlayer",
                componentName,
                null,
            )

        val playbackStateBuilder = PlaybackStateCompat.Builder()
        playbackStateBuilder.setActions(
            PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_FAST_FORWARD,
        )

        mediaSession.setPlaybackState(playbackStateBuilder.build())
        mediaSession.isActive = true
        playing = true
        play()
    }

    fun play() {
        if (playing) {
            exoPlayer?.playWhenReady = true
        }
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
    }

    fun stop() {
        exoPlayer?.stop()
        exoPlayer?.release()
        onStop()
    }
}
