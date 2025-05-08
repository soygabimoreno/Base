package soy.gabimoreno.presentation.screen.courses.detail

sealed interface AudioCourseDetailEvent {
    data class Error(val error: Throwable?) : AudioCourseDetailEvent
    data object PlayAudio : AudioCourseDetailEvent
}
