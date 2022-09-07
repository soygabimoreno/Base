package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class ErrorTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {

    data class EncodeUrlException(override val parameters: Map<String, Any>) :
        ErrorTrackerEvent(parameters)
}
