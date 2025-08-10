package soy.gabimoreno.presentation.screen.playlist.audio

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
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_PLAYLIST_AUDIO_ID
import soy.gabimoreno.data.tracker.main.PlaylistAudioTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.usecase.DeletePlaylistItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetAllPlaylistUseCase
import soy.gabimoreno.domain.usecase.GetAudioCourseItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetPlaylistByPlaylistItemIdUseCase
import soy.gabimoreno.domain.usecase.GetPodcastByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import soy.gabimoreno.domain.usecase.SetPlaylistItemsUseCase
import soy.gabimoreno.domain.util.AudioItemType
import soy.gabimoreno.domain.util.audioItemTypeDetector
import javax.inject.Inject

@HiltViewModel
class PlaylistAudioItemViewModel
    @Inject
    constructor(
        private val deletePlaylistItemUseCase: DeletePlaylistItemByIdUseCase,
        private val getAllPlaylistUseCase: GetAllPlaylistUseCase,
        private val getAudioCourseItemByIdUseCase: GetAudioCourseItemByIdUseCase,
        private val getPlaylistByPlaylistItemIdUseCase: GetPlaylistByPlaylistItemIdUseCase,
        private val getPodcastByIdUseCase: GetPodcastByIdUseCase,
        private val getPremiumAudioByIdUseCase: GetPremiumAudioByIdUseCase,
        private val setPlaylistItemsUseCase: SetPlaylistItemsUseCase,
        private val tracker: Tracker,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        var state by mutableStateOf(PlaylistAudioItemState())
            private set

        private val eventChannel = MutableSharedFlow<PlaylistAudioItemEvent>()
        val events = eventChannel.asSharedFlow()

        fun onViewScreen(audioItemId: String) {
            tracker.trackEvent(
                PlaylistAudioTrackerEvent.ViewScreen(
                    parameters = mapOf(TRACKER_KEY_PLAYLIST_AUDIO_ID to audioItemId),
                ),
            )
            state = state.copy(isLoading = true)
            viewModelScope.launch(dispatcher) {
                val selectedAudioTitle =
                    when (audioItemTypeDetector(audioItemId)) {
                        AudioItemType.AUDIO_COURSE -> {
                            val audioCourseItem =
                                getAudioCourseItemByIdUseCase(
                                    audioItemId,
                                ).getOrNull()
                            audioCourseItem?.title.orEmpty()
                        }
                        AudioItemType.PREMIUM_AUDIO -> {
                            val premiumAudio = getPremiumAudioByIdUseCase(audioItemId)
                            premiumAudio.getOrNull()?.title.orEmpty()
                        }
                        AudioItemType.PODCAST -> {
                            val podcast = getPodcastByIdUseCase(audioItemId)
                            podcast.getOrNull()?.title.orEmpty()
                        }
                    }

                val selectedPlaylists = getPlaylistByPlaylistItemIdUseCase(audioItemId).getOrNull()

                getAllPlaylistUseCase()
                    .onRight { playlists ->
                        val playlistsWithInfo: MutableList<PlaylistAudioItemState.PlaylistItem> =
                            mutableListOf()
                        playlists.map { playlist ->
                            val isSelected = selectedPlaylists?.contains(playlist.id) ?: false
                            val playlistInfo =
                                PlaylistAudioItemState.PlaylistItem(
                                    playlist = playlist,
                                    isSelected = isSelected,
                                    isInitiallySelected = isSelected,
                                )
                            playlistsWithInfo.add(playlistInfo)
                        }
                        state =
                            state.copy(
                                isLoading = false,
                                playlists = playlistsWithInfo,
                                selectedAudioId = audioItemId,
                                selectedAudioTitle = selectedAudioTitle,
                            )
                    }.onLeft {
                        eventChannel.emit(PlaylistAudioItemEvent.Error(it))
                        state =
                            state.copy(
                                isLoading = false,
                                selectedAudioId = audioItemId,
                                selectedAudioTitle = selectedAudioTitle,
                            )
                    }
            }
        }

        fun onAction(action: PlaylistAudioItemAction) {
            when (action) {
                is PlaylistAudioItemAction.OnTogglePlaylist -> {
                    state =
                        state.copy(
                            playlists =
                                state.playlists.map { playlist ->
                                    if (playlist.playlist.id == action.playlistId) {
                                        playlist.copy(isSelected = !playlist.isSelected)
                                    } else {
                                        playlist
                                    }
                                },
                        )
                }

                PlaylistAudioItemAction.OnSaveClicked -> {
                    viewModelScope.launch(dispatcher) {
                        val playlistsToInsert = mutableListOf<Int>()
                        val playlistsToRemove = mutableListOf<Int>()

                        state.playlists.forEach { playlist ->
                            if (playlist.hasBeenModified) {
                                if (playlist.isSelected) {
                                    playlistsToInsert.add(playlist.playlist.id)
                                } else {
                                    playlistsToRemove.add(playlist.playlist.id)
                                }
                            }
                        }

                        if (playlistsToInsert.isNotEmpty()) {
                            setPlaylistItemsUseCase(state.selectedAudioId, playlistsToInsert)
                        }

                        playlistsToRemove.forEach { playlistId ->
                            deletePlaylistItemUseCase(state.selectedAudioId, playlistId)
                        }
                        state =
                            state.copy(
                                playlists =
                                    state.playlists.map { playlist ->
                                        playlist.copy(isInitiallySelected = playlist.isSelected)
                                    },
                            )
                        eventChannel.emit(PlaylistAudioItemEvent.Success)
                    }
                }

                else -> Unit
            }
        }
    }
