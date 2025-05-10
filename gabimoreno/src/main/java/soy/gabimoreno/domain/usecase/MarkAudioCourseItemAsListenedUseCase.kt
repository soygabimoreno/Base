package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import javax.inject.Inject


class MarkAudioCourseItemAsListenedUseCase @Inject constructor(
    private val audioCoursesRepository: AudioCoursesRepository
) {
    suspend operator fun invoke(idAudioCourseItem: String, hasBeenListened: Boolean) {
        audioCoursesRepository.markAudioCourseItemAsListened(idAudioCourseItem, hasBeenListened)
    }
}
