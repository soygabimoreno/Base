package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class HomeTrackerEvent(
    override val parameters: Map<String, Any> = mapOf()
) : TrackerEvent {

    data class ViewScreen(override val parameters: Map<String, Any>) : HomeTrackerEvent()
    data class ClickEpisode(override val parameters: Map<String, Any>) : HomeTrackerEvent()
}
