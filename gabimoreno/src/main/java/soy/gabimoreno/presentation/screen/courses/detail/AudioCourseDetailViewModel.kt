package soy.gabimoreno.presentation.screen.courses.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.usecase.GetAudioCourseByIdUseCase
import javax.inject.Inject

@HiltViewModel
class AudioCourseDetailViewModel @Inject constructor(
    private val getAudioCourseByIdUseCase: GetAudioCourseByIdUseCase
) : ViewModel() {
    var state by mutableStateOf(AudioCourseDetailState())
        private set

    private val eventChannel = MutableSharedFlow<AudioCourseDetailEvent>()
    val events = eventChannel.asSharedFlow()

    fun onViewScreen(audioCourseId: String) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            getAudioCourseByIdUseCase(audioCourseId)
                .onRight { audioCourse ->
                    state = state.copy(isLoading = false, audioCourse = audioCourse)
                }
                .onLeft {
                    eventChannel.emit(AudioCourseDetailEvent.Error(it))
                    state = state.copy(isLoading = false)
                }
        }
    }

    fun onAction(action: AudioCourseDetailAction) {
        when (action) {
            is AudioCourseDetailAction.OnAudioCourseItemClicked -> {
                prepateToPlayAudio(action.audioCourseItem.id)
                viewModelScope.launch {
                    eventChannel.emit(AudioCourseDetailEvent.PlayAudio)
                }
            }

            else -> Unit
        }
    }

    private fun prepateToPlayAudio(audioCourseId: String) {
        var audio: Episode? = null
        val audios: List<Episode> = state.audioCourse?.let { audioCourse ->
            audioCourse.audios.map { audioCourseItem ->
                val episode = Episode(
                    id = audioCourseItem.id,
                    title = audioCourseItem.title,
                    description = audioCourse.description,
                    saga = audioCourse.saga,
                    url = audioCourseItem.url,
                    audioUrl = audioCourseItem.url,
                    imageUrl = audioCourse.thumbnailUrl,
                    thumbnailUrl = audioCourse.thumbnailUrl,
                    pubDateMillis = audioCourse.pubDateMillis,
                    audioLengthInSeconds = audioCourse.audioLengthInSeconds
                )
                if (episode.id == audioCourseId) audio = episode
                episode
            }
        } ?: emptyList()
        state = state.copy(audio = audio, audios = audios)
    }
}
