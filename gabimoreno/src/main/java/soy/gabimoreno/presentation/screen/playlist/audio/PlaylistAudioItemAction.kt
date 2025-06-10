package soy.gabimoreno.presentation.screen.playlist.audio

sealed interface PlaylistAudioItemAction {
    data object OnBackClicked : PlaylistAudioItemAction
    data class OnPlaylistAudioItemSelect(val playlistId: Int) : PlaylistAudioItemAction
    data object OnSaveClicked : PlaylistAudioItemAction
}
