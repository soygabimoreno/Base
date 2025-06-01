package soy.gabimoreno.presentation.screen.playlist.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.usecase.GetAllPlaylistUseCase
import soy.gabimoreno.domain.usecase.InsertPlaylistUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val getAllPlaylistUseCase: GetAllPlaylistUseCase,
    private val insertPlaylistUseCase: InsertPlaylistUseCase,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state
        .onStart {
            onScreenView()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = PlaylistState()
        )

    private val eventChannel = MutableSharedFlow<PlaylistEvent>()
    val events = eventChannel.asSharedFlow()

    private fun onScreenView() {
        _state.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch(dispatcher) {
            getAllPlaylistUseCase()
                .onRight { playlists ->
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            playlists = playlists,
                        )
                    }
                }
                .onLeft {
                    eventChannel.emit(PlaylistEvent.Error(it))
                    _state.update { currentState ->
                        currentState.copy(isLoading = false)
                    }
                }
        }
    }

    fun onAction(action: PlaylistAction) {
        when (action) {
            PlaylistAction.OnAddNewPlaylistClicked -> {
                _state.update { currentState ->
                    currentState.copy(shouldIShowAddPlaylistDialog = true)
                }
            }

            PlaylistAction.OnAddPlaylistDialogConfirm -> {
                if (state.value.dialogTitle.isBlank()) {
                    _state.update { currentState ->
                        currentState.copy(dialogTitleError = true)
                    }
                } else {
                    savePlaylist()
                }
            }

            PlaylistAction.OnAddPlaylistDialogDismiss -> {
                _state.update { currentState ->
                    currentState.copy(
                        shouldIShowAddPlaylistDialog = false,
                        dialogTitle = emptyString,
                        dialogTitleError = false,
                        dialogDescription = emptyString
                    )
                }
            }

            is PlaylistAction.OnDialogDescriptionChange -> {
                _state.update { currentState ->
                    currentState.copy(dialogDescription = action.description)
                }
            }

            is PlaylistAction.OnDialogTitleChange -> {
                _state.update { currentState ->
                    currentState.copy(
                        dialogTitle = action.title,
                        dialogTitleError = action.title.isEmpty()
                    )
                }
            }

            else -> Unit
        }
    }

    private fun savePlaylist() {
        val currentState = state.value
        _state.update {
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch(dispatcher) {
            insertPlaylistUseCase(
                name = state.value.dialogTitle,
                description = state.value.dialogDescription
            ).onRight { newPlaylist ->
                val updatedPlaylists = state.value.playlists + newPlaylist
                _state.update {
                    currentState.copy(
                        isLoading = false,
                        shouldIShowAddPlaylistDialog = false,
                        dialogTitle = emptyString,
                        dialogDescription = emptyString,
                        playlists = updatedPlaylists
                    )
                }
            }.onLeft {
                eventChannel.emit(PlaylistEvent.Error(it))
                _state.update {
                    currentState.copy(isLoading = false)
                }
            }
        }
    }
}

private const val STOP_TIMEOUT_MILLIS = 5000L
