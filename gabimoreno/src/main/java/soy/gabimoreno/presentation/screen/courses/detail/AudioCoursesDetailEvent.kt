package soy.gabimoreno.presentation.screen.courses.detail

sealed interface AudioCoursesDetailEvent {
    data class Error(val error: Throwable?) : AudioCoursesDetailEvent
    data class ShowPlayer(val audioCourseId: String) : AudioCoursesDetailEvent
}
