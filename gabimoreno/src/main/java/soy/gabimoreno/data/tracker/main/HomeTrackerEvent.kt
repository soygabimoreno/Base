package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class HomeTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {

    object ViewScreen : HomeTrackerEvent()
    data class ClickEpisode(override val parameters: Map<String, Any>) :
        HomeTrackerEvent(parameters)

    data class ReceiveDeepLink(override val parameters: Map<String, Any>) :
        HomeTrackerEvent(parameters)
}
