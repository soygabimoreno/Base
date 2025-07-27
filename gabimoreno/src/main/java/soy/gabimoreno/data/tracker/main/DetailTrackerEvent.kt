package soy.gabimoreno.data.tracker.main

import soy.gabimoreno.data.tracker.TrackerEvent

sealed class DetailTrackerEvent(
    override val parameters: Map<String, Any> = mapOf(),
) : TrackerEvent {
    data class ViewScreen(
        override val parameters: Map<String, Any>,
    ) : DetailTrackerEvent()
    data class ClickBack(
        override val parameters: Map<String, Any>,
    ) : DetailTrackerEvent()
    data class ClickPlay(
        override val parameters: Map<String, Any>,
    ) : DetailTrackerEvent()
    data class ClickPause(
        override val parameters: Map<String, Any>,
    ) : DetailTrackerEvent()
    data class ClickShare(
        override val parameters: Map<String, Any>,
    ) : DetailTrackerEvent()
    data class AddAudioToFavorite(
        override val parameters: Map<String, Any>,
    ) : DetailTrackerEvent()
    data class RemoveAudioFromFavorite(
        override val parameters: Map<String, Any>,
    ) : DetailTrackerEvent()
}
