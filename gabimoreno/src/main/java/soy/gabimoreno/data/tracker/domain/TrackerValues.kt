package soy.gabimoreno.data.tracker.domain

enum class PlayPause {
    PLAY,
    PAUSE
}

fun Boolean.toPlayPause() = if (this) PlayPause.PAUSE else PlayPause.PLAY
