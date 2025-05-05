package soy.gabimoreno.presentation.screen.courses.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class AudioCoursesDetailViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(AudioCoursesDetailState())
        private set

    private val eventChannel = MutableSharedFlow<AudioCoursesDetailEvent>()
    val events = eventChannel.asSharedFlow()

    init {
        onViewScreen()
    }

    private fun onViewScreen() {
        println("onViewScreen")
    }

    fun onAction(action: AudioCoursesDetailAction) {
        when (action) {
            AudioCoursesDetailAction.OnBackPressed -> TODO()
            is AudioCoursesDetailAction.OnPlayPauseClicked -> TODO()
        }
    }
}
