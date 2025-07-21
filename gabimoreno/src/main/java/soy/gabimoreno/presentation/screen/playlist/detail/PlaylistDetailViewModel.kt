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
        @IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        var state by mutableStateOf(PlaylistDetailState())
            private set

        private val eventChannel = MutableSharedFlow<PlaylistDetailEvent>()
        val events = eventChannel.asSharedFlow()

        fun onScreenView(playlistId: Int) {
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
                is PlaylistDetailAction.OnAudioItemClicked -> {
                    updateState { copy(audio = action.playlistAudioItem) }
                    emitAudio()
                }

                PlaylistDetailAction.OnPlayClicked -> {
                    if (state.playlistAudioItems.isNotEmpty()) {
                        updateState { copy(audio = playlistAudioItems.first()) }
                        emitAudio()
                    }
                }

                is PlaylistDetailAction.OnAudioItemsReordered -> {
                    if (action.playlistAudioItems != state.playlistAudioItems &&
                        state.playlist != null
                    ) {
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

                is PlaylistDetailAction.OnRemovePlaylistAudioItem -> {
                    updateState {
                        copy(
                            shouldIShowDialog = true,
                            dialogType = PlaylistDialogType.Delete,
                            selectedPlaylistAudioItem = action.playlistAudioItemId,
                        )
                    }
                }

                PlaylistDetailAction.OnConfirmDialog -> {
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
                                    playlistAudioItems =
                                        playlistAudioItems.filterNot {
                                            it.id ==
                                                audioId
                                        },
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

                is PlaylistDetailAction.OnDialogTitleChange -> {
                    updateState {
                        copy(
                            dialogTitle = action.title,
                            dialogTitleError = action.title.isEmpty(),
                        )
                    }
                }

                is PlaylistDetailAction.OnDialogDescriptionChange -> {
                    updateState {
                        copy(
                            dialogDescription = action.description,
                            dialogDescriptionError = action.description.isEmpty(),
                        )
                    }
                }

                PlaylistDetailAction.OnEditPlaylistClicked -> {
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

                PlaylistDetailAction.OnEditPlaylistConfirmDialog -> {
                    if (state.dialogTitle.isBlank()) {
                        updateState { copy(dialogTitleError = true) }
                    } else {
                        updatePlaylist()
                    }
                }

                PlaylistDetailAction.OnEditPlaylistDismissDialog -> {
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

                PlaylistDetailAction.OnDismissDialog -> {
                    updateState {
                        copy(
                            shouldIShowDialog = false,
                            selectedPlaylistAudioItem = null,
                        )
                    }
                }

                else -> Unit
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
