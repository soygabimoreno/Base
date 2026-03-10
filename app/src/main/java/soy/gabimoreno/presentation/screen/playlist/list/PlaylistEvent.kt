package soy.gabimoreno.presentation.screen.playlist.list

sealed interface PlaylistEvent {
    data class Error(
        val error: Throwable?,
    ) : PlaylistEvent
}
