package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class DetailTrackerEvent(
    override val parameters: Map<String, Any> = mapOf()
) : TrackerEvent {

    data class ScreenDetail(override val parameters: Map<String, Any>) : DetailTrackerEvent()
    data class ClickPlay(override val parameters: Map<String, Any>) : HomeTrackerEvent()
    data class ClickShare(override val parameters: Map<String, Any>) : HomeTrackerEvent()
}
