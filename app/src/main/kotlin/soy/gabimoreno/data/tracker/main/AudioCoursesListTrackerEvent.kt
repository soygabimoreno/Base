package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class AudioCoursesListTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {
    object ViewScreen : AudioCoursesListTrackerEvent()
}
