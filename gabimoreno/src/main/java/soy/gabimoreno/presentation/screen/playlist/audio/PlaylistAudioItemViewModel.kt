package soy.gabimoreno.presentation.screen.playlist.audio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.usecase.GetAudioCourseItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistAudioItemViewModel @Inject constructor(
    private val getPremiumAudioByIdUseCase: GetPremiumAudioByIdUseCase,
    private val getAudioCourseItemByIdUseCase: GetAudioCourseItemByIdUseCase,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    var state by mutableStateOf(PlaylistAudioItemState())
        private set

    fun onViewScreen(audioItemId: String) {
        state = state.copy(isLoading = true)
        viewModelScope.launch(dispatcher) {
            val selectedAudioTitle = if (audioItemId.contains("-")) {
                val audioCourseItem = getAudioCourseItemByIdUseCase(audioItemId).getOrNull()
                audioCourseItem?.title.orEmpty()
            } else {
                val premiumAudio = getPremiumAudioByIdUseCase(audioItemId)
                premiumAudio.getOrNull()?.title.orEmpty()
            }
            state = state.copy(
                isLoading = false,
                selectedAudioId = audioItemId,
                selectedAudioTitle = selectedAudioTitle
            )
        }
    }


    fun onAction(action: PlaylistAudioItemAction) {
        when (action) {
            is PlaylistAudioItemAction.OnPlaylistAudioItemSelect -> {

            }

            PlaylistAudioItemAction.OnSaveClicked -> {

            }

            else -> Unit
        }
    }
}
