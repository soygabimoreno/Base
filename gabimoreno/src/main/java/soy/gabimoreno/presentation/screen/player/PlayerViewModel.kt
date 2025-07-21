package soy.gabimoreno.presentation.screen.player

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_AUDIO_PLAYBACK_POSITION
import soy.gabimoreno.data.tracker.main.PlayerTrackerEvent
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.audio.Audio
import soy.gabimoreno.domain.session.MemberSession
import soy.gabimoreno.domain.usecase.CheckShouldIShowInAppReviewUseCase
import soy.gabimoreno.domain.usecase.MarkAudioCourseItemAsListenedUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase
import soy.gabimoreno.framework.KLog
import soy.gabimoreno.player.extension.currentPosition
import soy.gabimoreno.player.extension.isPlayEnabled
import soy.gabimoreno.player.extension.isPlaying
import soy.gabimoreno.player.service.MEDIA_ROOT_ID
import soy.gabimoreno.player.service.MediaPlayerService
import soy.gabimoreno.player.service.MediaPlayerServiceConnection
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel
    @Inject
    constructor(
        private val mediaPlayerServiceConnection: MediaPlayerServiceConnection,
        private val tracker: Tracker,
        private val memberSession: MemberSession,
        private val remoteConfigProvider: RemoteConfigProvider,
        private val markPremiumAudioAsListenedUseCase: MarkPremiumAudioAsListenedUseCase,
        private val markAudioCourseAsListenedUseCase: MarkAudioCourseItemAsListenedUseCase,
        private val checkShouldIShowInAppReviewUseCase: CheckShouldIShowInAppReviewUseCase,
        @IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        private val playbackState = mediaPlayerServiceConnection.playbackState

        val currentPlayingAudio = mediaPlayerServiceConnection.currentPlayingAudio

        var showPlayerFullScreen by mutableStateOf(false)

        private var currentPlaybackPosition by mutableLongStateOf(0L)

        internal var hasTriggeredEightyPercent by mutableStateOf(false)

        internal var lastAudioIdListened by mutableStateOf("")

        val podcastIsPlaying: Boolean
            get() = playbackState.value?.isPlaying == true

        val currentAudioProgress: Float
            get() {
                if (currentAudioDuration > 0) {
                    return currentPlaybackPosition.toFloat() / currentAudioDuration
                }
                return 0f
            }

        init {
            var previousAudioId: String? = null
            snapshotFlow { currentPlayingAudio.value?.id }
                .distinctUntilChanged()
                .onEach { newId ->
                    if (previousAudioId != newId) {
                        hasTriggeredEightyPercent = false
                        previousAudioId = newId
                    }
                }.launchIn(viewModelScope)
            viewModelScope.launch {
                mediaPlayerServiceConnection.progressFlow.collect { progress ->
                    if (progress >= SET_AUDIO_AS_LISTENED) {
                        markAudioAsListened(currentPlayingAudio.value?.id.orEmpty())
                    }
                }
            }
        }

        fun getCurrentPlaybackFormattedPosition(): String {
            KLog.d(
                "currentPlaybackPosition: $currentPlaybackPosition, ${
                    formatLong(
                        currentPlaybackPosition,
                    )
                }",
            )
            return formatLong(currentPlaybackPosition)
        }

        val currentAudioFormattedDuration: String
            get() =
                run {
                    KLog.d(
                        "currentAudioDuration: $currentAudioDuration, ${formatLong(
                            currentAudioDuration,
                        )}",
                    )
                    formatLong(currentAudioDuration)
                }

        private val currentAudioDuration: Long
            get() = MediaPlayerService.currentDuration

        override fun onCleared() {
            super.onCleared()
            mediaPlayerServiceConnection.unsubscribe(
                MEDIA_ROOT_ID,
                object : MediaBrowserCompat.SubscriptionCallback() {},
            )
        }

        fun onViewScreen(audio: Audio) {
            tracker.trackEvent(PlayerTrackerEvent.ViewScreen(audio.toMap()))
        }

        fun playPauseAudio(
            audios: List<Audio>,
            currentAudio: Audio,
        ) {
            mediaPlayerServiceConnection.playAudios(audios)
            if (currentAudio.id == currentPlayingAudio.value?.id) {
                if (podcastIsPlaying) {
                    onPause()
                } else {
                    onPlay()
                }
            } else {
                onPlayFromMediaId(currentAudio)
            }
        }

        fun togglePlaybackState() {
            when {
                playbackState.value?.isPlaying == true -> onPause()
                playbackState.value?.isPlayEnabled == true -> onPlay()
            }
        }

        fun onPlayPauseClickedFromPlayer(
            audio: Audio,
            playPause: PlayPause,
        ) {
            val parameters = audio.toMap()
            when (playPause) {
                PlayPause.PLAY ->
                    tracker.trackEvent(
                        PlayerTrackerEvent.ClickPlayFromPlayer(parameters),
                    )
                PlayPause.PAUSE ->
                    tracker.trackEvent(
                        PlayerTrackerEvent.ClickPauseFromPlayer(parameters),
                    )
            }
        }

        fun onPlayPauseClickedFromAudioBottomBar(
            audio: Audio,
            playPause: PlayPause,
        ) {
            val parameters = audio.toMap()
            when (playPause) {
                PlayPause.PLAY ->
                    tracker.trackEvent(
                        PlayerTrackerEvent.ClickPlayFromAudioBottomBar(
                            parameters,
                        ),
                    )

                PlayPause.PAUSE ->
                    tracker.trackEvent(
                        PlayerTrackerEvent.ClickPauseFromAudioBottomBar(
                            parameters,
                        ),
                    )
            }
        }

        fun onAudioBottomBarSwiped() {
            tracker.trackEvent(PlayerTrackerEvent.Stop(getParameters()))
        }

        fun stopPlayback() {
            mediaPlayerServiceConnection.transportControls.stop()
        }

        fun onRewindClicked() {
            tracker.trackEvent(PlayerTrackerEvent.ClickRewind(getParameters()))
            mediaPlayerServiceConnection.rewind()
        }

        fun onForwardClicked() {
            tracker.trackEvent(PlayerTrackerEvent.ClickForward(getParameters()))
            mediaPlayerServiceConnection.fastForward()
        }

        fun onSkipToPrevious() {
            tracker.trackEvent(PlayerTrackerEvent.ClickSkipToPrevious(getParameters()))
            mediaPlayerServiceConnection.skipToPrevious()
        }

        fun onSkipToNext() {
            tracker.trackEvent(PlayerTrackerEvent.ClickSkipToNext(getParameters()))
            mediaPlayerServiceConnection.skipToNext()
        }

        /**
         * @param value from 0.0 to 1.0
         */
        fun onSliderChangeFinished(value: Float) {
            val playbackPosition = (currentAudioDuration * value).toLong()
            val parameters =
                getParameters() +
                    mapOf(
                        TRACKER_KEY_AUDIO_PLAYBACK_POSITION to formatLong(playbackPosition),
                    )
            tracker.trackEvent(PlayerTrackerEvent.SeekTo(parameters))
            mediaPlayerServiceConnection.transportControls.seekTo(playbackPosition)
        }

        suspend fun updateCurrentPlaybackPosition() {
            val currentPosition = playbackState.value?.currentPosition
            if (currentPosition != null && currentPosition != currentPlaybackPosition) {
                currentPlaybackPosition = currentPosition
            }
            delay(PLAYBACK_POSITION_UPDATE_INTERVAL)
            updateCurrentPlaybackPosition()
        }

        private fun markAudioAsListened(audioId: String) {
            if (hasTriggeredEightyPercent || lastAudioIdListened == audioId) return

            hasTriggeredEightyPercent = true
            lastAudioIdListened = audioId

            viewModelScope.launch {
                checkShouldIShowInAppReviewUseCase()
                if (audioId.contains("-")) {
                    markAudioCourseAsListenedUseCase(audioId, true)
                } else {
                    markPremiumAudioAsListenedUseCase(audioId, true)
                }
            }
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

        private fun onPlayFromMediaId(currentAudio: Audio) {
            val parameters = currentAudio.toMap()
            tracker.trackEvent(PlayerTrackerEvent.PlayFromMediaId(parameters))
            mediaPlayerServiceConnection.transportControls.playFromMediaId(currentAudio.id, null)
        }

        private fun getParameters(): Map<String, String> =
            currentPlayingAudio.value?.toMap() ?: mapOf()
    }

private const val PLAYBACK_POSITION_UPDATE_INTERVAL = 1_000L
private const val SET_AUDIO_AS_LISTENED = 0.8
