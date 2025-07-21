package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class PlayerTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {
    data class ViewScreen(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class Play(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class PlayFromMediaId(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class Pause(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class Stop(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class ClickRewind(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class ClickForward(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class ClickSkipToPrevious(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class ClickSkipToNext(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class SeekTo(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class ClickPlayFromPlayer(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
    data class ClickPlayFromAudioBottomBar(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()

    data class ClickPauseFromPlayer(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()

    data class ClickPauseFromAudioBottomBar(
        override val parameters: Map<String, Any>,
    ) : PlayerTrackerEvent()
}
