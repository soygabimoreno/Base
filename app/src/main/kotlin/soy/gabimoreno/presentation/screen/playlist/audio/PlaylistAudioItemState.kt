package soy.gabimoreno.presentation.screen.playlist.audio

import soy.gabimoreno.domain.model.content.Playlist

data class PlaylistAudioItemState(
    val isLoading: Boolean = true,
    val selectedAudioId: String = "",
    val selectedAudioTitle: String = "",
    val playlists: List<PlaylistItem> = emptyList(),
) {
    data class PlaylistItem(
        val playlist: Playlist,
        val isSelected: Boolean,
        val isInitiallySelected: Boolean,
    ) {
        val hasBeenModified: Boolean get() = isSelected != isInitiallySelected
    }
}
