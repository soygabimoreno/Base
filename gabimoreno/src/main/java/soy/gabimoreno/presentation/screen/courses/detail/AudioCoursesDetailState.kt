package soy.gabimoreno.presentation.screen.courses.detail

import soy.gabimoreno.domain.model.content.AudioCourse

data class AudioCoursesDetailState(
    val isLoading: Boolean = true,
    val audioCourse: AudioCourse? = null
)
