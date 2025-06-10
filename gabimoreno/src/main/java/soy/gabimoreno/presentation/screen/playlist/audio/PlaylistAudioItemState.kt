package soy.gabimoreno.presentation.screen.playlist.audio

import soy.gabimoreno.domain.model.content.Playlist

data class PlaylistAudioItemState(
    val isLoading: Boolean = true,
    val selectedPlaylists: List<Playlist> = emptyList(),
    val selectedAudioId: String = "",
    val selectedAudioTitle: String = "",
)
