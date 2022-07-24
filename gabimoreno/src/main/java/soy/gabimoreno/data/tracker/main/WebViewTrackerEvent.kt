package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class WebViewTrackerEvent(
    override val parameters: Map<String, Any> = mapOf()
) : TrackerEvent {

    data class ViewScreen(override val parameters: Map<String, Any>) : WebViewTrackerEvent()
    data class ClickBack(override val parameters: Map<String, Any>) : WebViewTrackerEvent()
    data class ViewPage(override val parameters: Map<String, Any>) : WebViewTrackerEvent()
}
