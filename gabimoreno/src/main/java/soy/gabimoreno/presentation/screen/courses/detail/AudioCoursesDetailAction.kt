package soy.gabimoreno.presentation.screen.courses.detail

import soy.gabimoreno.domain.model.audio.Audio

sealed interface AudioCoursesDetailAction {
    data class OnPlayPauseClicked(val audio: Audio) : AudioCoursesDetailAction
    data object OnBackPressed : AudioCoursesDetailAction
}
