package soy.gabimoreno.domain.usecase

import soy.gabimoreno.core.camelToSnakeUpperCase
import soy.gabimoreno.data.tracker.TrackerEvent
import javax.inject.Inject

class GetTrackingEventNameUseCase
    @Inject
    constructor() {
        operator fun invoke(trackerEvent: TrackerEvent): String {
            val javaClass = trackerEvent.javaClass
            val screen =
                javaClass.superclass.simpleName
                    .toString()
                    .replace(TRACKER_EVENT_SUFFIX, EMPTY_STRING)
                    .camelToSnakeUpperCase()
            val action = javaClass.simpleName.toString().camelToSnakeUpperCase()
            return "${screen}_$action"
        }
    }

private const val TRACKER_EVENT_SUFFIX = "TrackerEvent"
private const val EMPTY_STRING = ""
