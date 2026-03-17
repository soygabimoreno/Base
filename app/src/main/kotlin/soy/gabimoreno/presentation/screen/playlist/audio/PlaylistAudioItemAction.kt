package soy.gabimoreno.presentation.screen.playlist.audio

sealed interface PlaylistAudioItemAction {
    data object OnBackClick : PlaylistAudioItemAction
    data class OnTogglePlaylist(
        val playlistId: Int,
    ) : PlaylistAudioItemAction

    data object OnSaveClick : PlaylistAudioItemAction
    data object OnNewPlaylistClick : PlaylistAudioItemAction
}
