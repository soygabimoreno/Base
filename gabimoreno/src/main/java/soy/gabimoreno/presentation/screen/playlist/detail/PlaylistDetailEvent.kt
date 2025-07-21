package soy.gabimoreno.presentation.screen.playlist.detail

sealed interface PlaylistDetailEvent {
    data class Error(
        val error: Throwable?,
    ) : PlaylistDetailEvent
    data object PlayAudio : PlaylistDetailEvent
}
