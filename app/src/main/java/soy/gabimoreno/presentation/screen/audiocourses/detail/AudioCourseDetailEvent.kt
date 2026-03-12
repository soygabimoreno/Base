package soy.gabimoreno.presentation.screen.audiocourses.detail

sealed interface AudioCourseDetailEvent {
    data class Error(
        val error: Throwable?,
    ) : AudioCourseDetailEvent

    data object PlayAudio : AudioCourseDetailEvent
}
