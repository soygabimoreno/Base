package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class NotificationTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {

    data class OnNewToken(override val parameters: Map<String, Any>) : NotificationTrackerEvent()
    data class OnSubscribedToTopic(override val parameters: Map<String, Any>) :
        NotificationTrackerEvent()
}
