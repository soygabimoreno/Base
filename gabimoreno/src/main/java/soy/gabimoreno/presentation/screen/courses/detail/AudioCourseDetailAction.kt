package soy.gabimoreno.presentation.screen.courses.detail

import soy.gabimoreno.domain.model.content.AudioCourseItem

sealed interface AudioCourseDetailAction {
    data class OnAudioCourseItemClicked(
        val audioCourseItem: AudioCourseItem,
    ) : AudioCourseDetailAction
    data class OnAudioItemListenedToggled(
        val audioCourseItem: AudioCourseItem,
    ) : AudioCourseDetailAction
    data object OnBackClicked : AudioCourseDetailAction
    data class OnAddToPlaylistClicked(
        val audioCourseId: String,
    ) : AudioCourseDetailAction
    data class OnFavoriteStatusChanged(
        val audioCourseItem: AudioCourseItem,
    ) : AudioCourseDetailAction
}
