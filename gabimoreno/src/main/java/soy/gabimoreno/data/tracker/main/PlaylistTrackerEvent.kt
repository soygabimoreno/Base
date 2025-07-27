package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class PlaylistTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {
    object ViewScreen : PlaylistTrackerEvent()
}
