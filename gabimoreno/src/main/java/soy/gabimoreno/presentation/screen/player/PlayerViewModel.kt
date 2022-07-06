package soy.gabimoreno.presentation.screen.player

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PLAY_PAUSE
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.main.PlayerTrackerEvent.*
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.framework.KLog
import soy.gabimoreno.player.extension.currentPosition
import soy.gabimoreno.player.extension.isPlayEnabled
import soy.gabimoreno.player.extension.isPlaying
import soy.gabimoreno.player.service.MEDIA_ROOT_ID
import soy.gabimoreno.player.service.MediaPlayerService
import soy.gabimoreno.player.service.MediaPlayerServiceConnection
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val serviceConnection: MediaPlayerServiceConnection,
    private val tracker: Tracker
) : ViewModel() {

    fun onViewScreen(episode: Episode) {
        tracker.trackEvent(ViewScreen(episode.toMap()))
    }

    private fun onPlay(episode: Episode) {
        tracker.trackEvent(
            Play(episode.toMap())
        )
    }

    private fun onPause(episode: Episode) {
        tracker.trackEvent(Pause(episode.toMap()))
    }

    private fun onPlayFromMediaId(episode: Episode) {
        tracker.trackEvent(PlayFromMediaId(episode.toMap()))
    }

    val currentPlayingEpisode = serviceConnection.currentPlayingEpisode

    var showPlayerFullScreen by mutableStateOf(false)

    var currentPlaybackPosition by mutableStateOf(0L)

    val podcastIsPlaying: Boolean
        get() = playbackState.value?.isPlaying == true

    val currentEpisodeProgress: Float
        get() {
            if (currentEpisodeDuration > 0) {
                return currentPlaybackPosition.toFloat() / currentEpisodeDuration
            }
            return 0f
        }

    val currentPlaybackFormattedPosition: String
        get() = run {
            KLog.d("currentPlaybackPosition: $currentPlaybackPosition, ${formatLong(currentPlaybackPosition)}")
            formatLong(currentPlaybackPosition)
        }

    val currentEpisodeFormattedDuration: String
        get() = run {
            KLog.d("currentEpisodeDuration: $currentEpisodeDuration, ${formatLong(currentEpisodeDuration)}")
            formatLong(currentEpisodeDuration)
        }

    private val playbackState = serviceConnection.playbackState

    private val currentEpisodeDuration: Long
        get() = MediaPlayerService.currentDuration

    fun playPauseEpisode(
        episodes: List<Episode>,
        currentEpisode: Episode
    ) {
        serviceConnection.playPodcast(episodes)
        if (currentEpisode.id == currentPlayingEpisode.value?.id) {
            if (podcastIsPlaying) {
                onPause(currentEpisode)
                serviceConnection.transportControls.pause()
            } else {
                onPlay(currentEpisode)
                serviceConnection.transportControls.play()
            }
        } else {
            onPlayFromMediaId(currentEpisode)
            serviceConnection.transportControls.playFromMediaId(currentEpisode.id, null)
        }
    }

    fun togglePlaybackState() {
        when {
            playbackState.value?.isPlaying == true -> {
                serviceConnection.transportControls.pause()
            }
            playbackState.value?.isPlayEnabled == true -> {
                serviceConnection.transportControls.play()
            }
        }
    }

    fun onPlayPauseClicked(
        episode: Episode,
        playPause: PlayPause
    ) {
        tracker.trackEvent(
            ClickPlayPause(
                episode.toMap() +
                    mapOf(
                        PLAY_PAUSE to playPause.toString()
                    )
            )
        )
    }

    fun stopPlayback() {
        serviceConnection.transportControls.stop()
    }

    fun calculateColorPalette(
        drawable: Drawable,
        onFinished: (Color) -> Unit
    ) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            palette?.darkVibrantSwatch?.rgb?.let { colorValue ->
                onFinished(Color(colorValue))
            }
        }
    }

    fun fastForward() {
        serviceConnection.fastForward()
    }

    fun rewind() {
        serviceConnection.rewind()
    }

    /**
     * @param value 0.0 to 1.0
     */
    fun seekToFraction(value: Float) {
        serviceConnection.transportControls.seekTo(
            (currentEpisodeDuration * value).toLong()
        )
    }

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(PLAYBACK_POSITION_UPDATE_INTERVAL)
        updateCurrentPlaybackPosition()
    }

    private fun formatLong(value: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+0")
        return dateFormat.format(value)
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}

private const val PLAYBACK_POSITION_UPDATE_INTERVAL = 1000L
