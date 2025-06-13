package soy.gabimoreno.presentation.screen.playlist.audio

sealed interface PlaylistAudioItemEvent {
    data class Error(val error: Throwable?) : PlaylistAudioItemEvent
    data object Success : PlaylistAudioItemEvent
}
