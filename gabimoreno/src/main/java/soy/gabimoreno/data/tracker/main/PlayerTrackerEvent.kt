package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class PlayerTrackerEvent(
    override val parameters: Map<String, Any> = mapOf()
) : TrackerEvent {

    data class ViewScreen(override val parameters: Map<String, Any>) : PlayerTrackerEvent()
    data class Play(override val parameters: Map<String, Any>) : PlayerTrackerEvent()
    data class Pause(override val parameters: Map<String, Any>) : PlayerTrackerEvent()
    data class PlayFromMediaId(override val parameters: Map<String, Any>) : PlayerTrackerEvent()
}
