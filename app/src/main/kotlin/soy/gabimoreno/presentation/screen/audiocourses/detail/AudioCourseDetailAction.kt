package soy.gabimoreno.presentation.screen.audiocourses.detail

import soy.gabimoreno.domain.model.content.AudioCourseItem

sealed interface AudioCourseDetailAction {
    data class OnAudioCourseItemClick(
        val audioCourseItem: AudioCourseItem,
    ) : AudioCourseDetailAction

    data class OnAudioItemListenedToggled(
        val audioCourseItem: AudioCourseItem,
    ) : AudioCourseDetailAction

    data object OnBackClick : AudioCourseDetailAction
    data class OnAddToPlaylistClick(
        val audioCourseId: String,
    ) : AudioCourseDetailAction

    data class OnFavoriteStatusChanged(
        val audioCourseItem: AudioCourseItem,
    ) : AudioCourseDetailAction

    data class OpenAudioCourseOnWeb(
        val audioCourseId: String,
    ) : AudioCourseDetailAction
}
