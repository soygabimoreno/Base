package soy.gabimoreno.presentation.screen.audiocourses.list

import soy.gabimoreno.domain.model.content.AudioCourse

data class AudioCoursesState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val audioCourses: List<AudioCourse> = emptyList(),
    val hasRefreshTokenBeenCalled: Boolean = false,
)
