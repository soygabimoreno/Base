package soy.gabimoreno.presentation.screen.audiocourses.list

sealed interface AudioCoursesEvent {
    data class Error(
        val error: Throwable?,
    ) : AudioCoursesEvent

    data object ShowTokenExpiredError : AudioCoursesEvent
    data class ShowDetail(
        val audioCourseId: String,
    ) : AudioCoursesEvent
}
