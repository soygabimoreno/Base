package soy.gabimoreno.presentation.screen.playlist.detail

sealed interface PlaylistDetailAction {
    data object OnBackClicked : PlaylistDetailAction
}
