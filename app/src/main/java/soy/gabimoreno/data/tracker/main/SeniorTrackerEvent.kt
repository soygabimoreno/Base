package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class SeniorTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {
    object ViewScreen : SeniorTrackerEvent()
    data class ClickSeniorAudio(
        override val parameters: Map<String, Any>,
    ) : SeniorTrackerEvent(parameters)

    data class ReceiveDeepLink(
        override val parameters: Map<String, Any>,
    ) : SeniorTrackerEvent(parameters)
}
