package soy.gabimoreno.presentation.screen.player

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.main.PlayerTrackerEvent
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
    private val mediaPlayerServiceConnection: MediaPlayerServiceConnection,
    private val tracker: Tracker
) : ViewModel() {

    private val playbackState = mediaPlayerServiceConnection.playbackState

    val currentPlayingEpisode = mediaPlayerServiceConnection.currentPlayingEpisode

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

    private val currentEpisodeDuration: Long
        get() = MediaPlayerService.currentDuration

    override fun onCleared() {
        super.onCleared()
        mediaPlayerServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }

    fun onViewScreen(episode: Episode) {
        tracker.trackEvent(PlayerTrackerEvent.ViewScreen(episode.toMap()))
    }

    fun playPauseEpisode(
        episodes: List<Episode>,
        currentEpisode: Episode
    ) {
        mediaPlayerServiceConnection.playPodcast(episodes)
        if (currentEpisode.id == currentPlayingEpisode.value?.id) {
            if (podcastIsPlaying) {
                onPause()
            } else {
                onPlay()
            }
        } else {
            onPlayFromMediaId(currentEpisode)
        }
    }

    fun togglePlaybackState() {
        when {
            playbackState.value?.isPlaying == true -> onPause()
            playbackState.value?.isPlayEnabled == true -> onPlay()
        }
    }

    fun onPlayPauseClickedFromPlayer(
        episode: Episode,
        playPause: PlayPause
    ) {
        val parameters = episode.toMap()
        when (playPause) {
            PlayPause.PLAY -> tracker.trackEvent(PlayerTrackerEvent.ClickPlayFromPlayer(parameters))
            PlayPause.PAUSE -> tracker.trackEvent(PlayerTrackerEvent.ClickPauseFromPlayer(parameters))
        }
    }

    fun onPlayPauseClickedFromAudioBottomBar(
        episode: Episode,
        playPause: PlayPause
    ) {
        val parameters = episode.toMap()
        when (playPause) {
            PlayPause.PLAY -> tracker.trackEvent(PlayerTrackerEvent.ClickPlayFromAudioBottomBar(parameters))
            PlayPause.PAUSE -> tracker.trackEvent(PlayerTrackerEvent.ClickPauseFromAudioBottomBar(parameters))
        }
    }

    fun onAudioBottomBarSwiped() {
        tracker.trackEvent(PlayerTrackerEvent.Stop(getParameters()))
    }

    fun stopPlayback() {
        mediaPlayerServiceConnection.transportControls.stop()
    }

    fun fastForward() {
        mediaPlayerServiceConnection.fastForward()
    }

    fun rewind() {
        mediaPlayerServiceConnection.rewind()
    }

    /**
     * @param value 0.0 to 1.0
     */
    fun seekToFraction(value: Float) {
        mediaPlayerServiceConnection.transportControls.seekTo(
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

    private fun onPlay() {
        tracker.trackEvent(PlayerTrackerEvent.Play(getParameters()))
        mediaPlayerServiceConnection.transportControls.play()
    }

    private fun onPause() {
        tracker.trackEvent(PlayerTrackerEvent.Pause(getParameters()))
        mediaPlayerServiceConnection.transportControls.pause()
    }

    private fun onPlayFromMediaId(currentEpisode: Episode) {
        val parameters = currentEpisode.toMap()
        tracker.trackEvent(PlayerTrackerEvent.PlayFromMediaId(parameters))
        mediaPlayerServiceConnection.transportControls.playFromMediaId(currentEpisode.id, null)
    }

    private fun getParameters(): Map<String, String> {
        return currentPlayingEpisode.value?.toMap() ?: mapOf()
    }
}

private const val PLAYBACK_POSITION_UPDATE_INTERVAL = 1000L
