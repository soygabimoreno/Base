package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class AudioCourseDetailTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {
    data class ViewScreen(
        override val parameters: Map<String, Any>,
    ) : AudioCourseDetailTrackerEvent()

    data class ViewOnWebScreen(
        override val parameters: Map<String, Any>,
    ) : AudioCourseDetailTrackerEvent()
}
