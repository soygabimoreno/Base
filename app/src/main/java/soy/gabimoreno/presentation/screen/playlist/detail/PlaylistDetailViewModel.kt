@file:Suppress("TooManyFunctions")

package soy.gabimoreno.presentation.screen.playlist.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_PLAYLIST_ID
import soy.gabimoreno.data.tracker.main.PlaylistDetailTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.usecase.DeletePlaylistItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetPlaylistByIdUseCase
import soy.gabimoreno.domain.usecase.UpdatePlaylistItemsUseCase
import soy.gabimoreno.domain.usecase.UpsertPlaylistsUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel
    @Inject
    constructor(
        private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
        private val updatePlaylistItemsUseCase: UpdatePlaylistItemsUseCase,
        private val deletePlaylistItemByIdUseCase: DeletePlaylistItemByIdUseCase,
        private val upsertPlaylistsUseCase: UpsertPlaylistsUseCase,
        private val tracker: Tracker,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        var state by mutableStateOf(PlaylistDetailState())
            private set

        private val eventChannel = MutableSharedFlow<PlaylistDetailEvent>()
        val events = eventChannel.asSharedFlow()

        fun onScreenView(playlistId: Int) {
            tracker.trackEvent(
                PlaylistDetailTrackerEvent.ViewScreen(
                    parameters = mapOf(TRACKER_KEY_PLAYLIST_ID to playlistId),
                ),
            )
            updateState { copy(isLoading = true) }

            viewModelScope.launch(dispatcher) {
                getPlaylistByIdUseCase(playlistId)
                    .onRight { playlist ->
                        updateState {
                            copy(
                                isLoading = false,
                                playlist = playlist,
                                playlistAudioItems = playlist?.items.orEmpty(),
                            )
                        }
                    }.onLeft {
                        emitError(it)
                        updateState { copy(isLoading = false) }
                    }
            }
        }

        fun onAction(action: PlaylistDetailAction) {
            when (action) {
                is PlaylistDetailAction.OnAudioItemClicked -> handleAudioItemClicked(action)
                PlaylistDetailAction.OnPlayClicked -> handlePlayClicked()
                is PlaylistDetailAction.OnAudioItemsReordered -> handleAudioItemsReordered(action)
                is PlaylistDetailAction.OnRemovePlaylistAudioItem -> handleRemoveAudioItem(action)
                PlaylistDetailAction.OnConfirmDialog -> handleConfirmDialog()
                is PlaylistDetailAction.OnDialogTitleChange -> handleDialogTitleChange(action)
                is PlaylistDetailAction.OnDialogDescriptionChange ->
                    handleDialogDescriptionChange(
                        action,
                    )

                PlaylistDetailAction.OnEditPlaylistClicked -> handleEditPlaylistClicked()
                PlaylistDetailAction.OnEditPlaylistConfirmDialog -> handleEditConfirmDialog()
                PlaylistDetailAction.OnEditPlaylistDismissDialog -> handleEditDismissDialog()
                PlaylistDetailAction.OnDismissDialog -> handleDismissDialog()
                else -> Unit
            }
        }

        private fun handleAudioItemClicked(action: PlaylistDetailAction.OnAudioItemClicked) {
            updateState { copy(audio = action.playlistAudioItem) }
            emitAudio()
        }

        private fun handlePlayClicked() {
            if (state.playlistAudioItems.isNotEmpty()) {
                updateState { copy(audio = playlistAudioItems.first()) }
                emitAudio()
            }
        }

        private fun handleAudioItemsReordered(action: PlaylistDetailAction.OnAudioItemsReordered) {
            if (action.playlistAudioItems != state.playlistAudioItems && state.playlist != null) {
                updateState {
                    copy(
                        playlistAudioItems = action.playlistAudioItems,
                        isLoading = true,
                    )
                }

                viewModelScope.launch(dispatcher) {
                    updatePlaylistItemsUseCase(
                        playlistId = state.playlist!!.id,
                        playlistItems = action.playlistAudioItems,
                    ).onRight {
                        updateState { copy(isLoading = false) }
                    }.onLeft {
                        emitError(it)
                        updateState { copy(isLoading = false) }
                    }
                }
            }
        }

        private fun handleRemoveAudioItem(action: PlaylistDetailAction.OnRemovePlaylistAudioItem) {
            updateState {
                copy(
                    shouldIShowDialog = true,
                    dialogType = PlaylistDialogType.Delete,
                    selectedPlaylistAudioItem = action.playlistAudioItemId,
                )
            }
        }

        private fun handleConfirmDialog() {
            val playlistId = state.playlist?.id ?: return
            val audioId = state.selectedPlaylistAudioItem ?: return

            updateState { copy(isLoading = true) }

            viewModelScope.launch {
                deletePlaylistItemByIdUseCase(
                    audioItemId = audioId,
                    playlistId = playlistId,
                ).onRight {
                    updateState {
                        copy(
                            playlistAudioItems = playlistAudioItems.filterNot { it.id == audioId },
                            selectedPlaylistAudioItem = null,
                            shouldIShowDialog = false,
                            isLoading = false,
                        )
                    }
                }.onLeft {
                    emitError(it)
                    updateState { copy(isLoading = false) }
                }
            }
        }

        private fun handleDialogTitleChange(action: PlaylistDetailAction.OnDialogTitleChange) {
            updateState {
                copy(
                    dialogTitle = action.title,
                    dialogTitleError = action.title.isEmpty(),
                )
            }
        }

        private fun handleDialogDescriptionChange(
            action: PlaylistDetailAction.OnDialogDescriptionChange,
        ) {
            updateState {
                copy(
                    dialogDescription = action.description,
                    dialogDescriptionError = action.description.isEmpty(),
                )
            }
        }

        private fun handleEditPlaylistClicked() {
            val playlist = state.playlist ?: return
            updateState {
                copy(
                    shouldIShowDialog = true,
                    dialogType = PlaylistDialogType.Edit,
                    dialogTitle = playlist.title,
                    dialogDescription = playlist.description,
                )
            }
        }

        private fun handleEditConfirmDialog() {
            if (state.dialogTitle.isBlank()) {
                updateState { copy(dialogTitleError = true) }
            } else {
                updatePlaylist()
            }
        }

        private fun handleEditDismissDialog() {
            updateState {
                copy(
                    shouldIShowDialog = false,
                    selectedPlaylistAudioItem = null,
                    dialogTitle = "",
                    dialogDescription = "",
                    dialogTitleError = false,
                    dialogDescriptionError = false,
                )
            }
        }

        private fun handleDismissDialog() {
            updateState {
                copy(
                    shouldIShowDialog = false,
                    selectedPlaylistAudioItem = null,
                )
            }
        }

        private fun updatePlaylist() {
            val current = state.playlist ?: return

            updateState { copy(isLoading = true) }

            viewModelScope.launch {
                upsertPlaylistsUseCase(
                    listOf(
                        current.copy(
                            title = state.dialogTitle,
                            description = state.dialogDescription,
                        ),
                    ),
                ).onRight {
                    updateState {
                        copy(
                            playlist =
                                current.copy(
                                    title = dialogTitle,
                                    description = dialogDescription,
                                ),
                            shouldIShowDialog = false,
                            isLoading = false,
                        )
                    }
                }.onLeft {
                    emitError(it)
                    updateState { copy(isLoading = false) }
                }
            }
        }

        private fun emitAudio() {
            viewModelScope.launch { eventChannel.emit(PlaylistDetailEvent.PlayAudio) }
        }

        private fun emitError(error: Throwable) {
            viewModelScope.launch { eventChannel.emit(PlaylistDetailEvent.Error(error)) }
        }

        private inline fun updateState(update: PlaylistDetailState.() -> PlaylistDetailState) {
            state = state.update()
        }
    }
