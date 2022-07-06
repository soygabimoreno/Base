package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class DetailTrackerEvent(
    override val parameters: Map<String, Any> = mapOf()
) : TrackerEvent {

    data class ViewScreen(override val parameters: Map<String, Any>) : DetailTrackerEvent()
    data class ClickPlayPause(override val parameters: Map<String, Any>) : DetailTrackerEvent()
    data class ClickShare(override val parameters: Map<String, Any>) : DetailTrackerEvent()
    data class ClickInfo(override val parameters: Map<String, Any>) : DetailTrackerEvent()
}
