package soy.gabimoreno.presentation.screen.detail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import soy.gabimoreno.R
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.main.DetailTrackerEvent
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.domain.model.audio.Audio
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.usecase.UpdateAudioItemFavoriteStateUseCase
import soy.gabimoreno.framework.intent.StartChooser
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val tracker: Tracker,
    private val startChooser: StartChooser,
    private val updateAudioItemFavoriteStateUseCase: UpdateAudioItemFavoriteStateUseCase,
) : ViewModel() {
    var audioState by mutableStateOf<Audio?>(null)

    fun onViewScreen(audio: Audio) {
        audioState = audio
        tracker.trackEvent(DetailTrackerEvent.ViewScreen(audio.toMap()))
    }

    fun onBackClicked(audio: Audio) {
        tracker.trackEvent(DetailTrackerEvent.ClickBack(audio.toMap()))
    }

    fun onPlayPauseClicked(
        audio: Audio,
        playPause: PlayPause,
    ) {
        val parameters = audio.toMap()
        when (playPause) {
            PlayPause.PLAY -> tracker.trackEvent(DetailTrackerEvent.ClickPlay(parameters))
            PlayPause.PAUSE -> tracker.trackEvent(DetailTrackerEvent.ClickPause(parameters))
        }
    }

    fun onShareClicked(
        context: Context,
        audio: Audio,
    ) {
        tracker.trackEvent(DetailTrackerEvent.ClickShare(audio.toMap()))
        startChooser(
            context,
            chooserTitleResId = R.string.share_podcast_content,
            title = audio.title,
            url = audio.url
        )
    }

    fun onFavoriteStatusChanged() {
        viewModelScope.launch {
            val currentState = audioState
            if (currentState != null && currentState is PremiumAudio) {
                val newState = currentState.copy(markedAsFavorite = !currentState.markedAsFavorite)
                audioState = newState
                updateAudioItemFavoriteStateUseCase(newState.id, newState.markedAsFavorite)
            }
        }
    }
}
