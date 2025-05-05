package soy.gabimoreno.presentation.screen.courses.detail

import soy.gabimoreno.domain.model.content.AudioCourseItem

sealed interface AudioCourseDetailAction {
    data class OnAudioCourseItemClicked(val audioCourseItem: AudioCourseItem) : AudioCourseDetailAction
    data object OnBackClicked : AudioCourseDetailAction
}
