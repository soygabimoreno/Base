package soy.gabimoreno.presentation.screen.courses.list

import soy.gabimoreno.domain.model.content.AudioCourse

data class AudioCoursesListState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val audiocourses: List<AudioCourse> = emptyList(),
)
