package soy.gabimoreno.presentation.screen.courses.list

sealed interface AudioCoursesListEvent {
    data class Error(val error: Throwable?) : AudioCoursesListEvent
    data object ShowTokenExpiredError : AudioCoursesListEvent
    data class ShowDetail(val audioCourseId: String) : AudioCoursesListEvent
}
