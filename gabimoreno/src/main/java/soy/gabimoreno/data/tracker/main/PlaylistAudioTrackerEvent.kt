package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class PlaylistAudioTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {
    data class ViewScreen(
        override val parameters: Map<String, Any>,
    ) : PlaylistAudioTrackerEvent()
}
