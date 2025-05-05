package soy.gabimoreno.presentation.screen.courses.detail

import soy.gabimoreno.domain.model.audio.Audio
import soy.gabimoreno.domain.model.content.AudioCourse

data class AudioCourseDetailState(
    val isLoading: Boolean = true,
    val audioCourse: AudioCourse? = null,
    val audio: Audio? = null,
    val audios: List<Audio> = emptyList(),
)
