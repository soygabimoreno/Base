package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class PlayerTrackerEvent(
    override val parameters: Map<String, Any> = mapOf()
) : TrackerEvent {

    data class ScreenPlayer(override val parameters: Map<String, Any>) : PlayerTrackerEvent()
}
