package soy.gabimoreno.presentation.screen.playlist.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(PlaylistDetailState())
        private set

    fun onAction(action: PlaylistDetailAction) {
        when (action) {
            else -> Unit
        }
    }
}
