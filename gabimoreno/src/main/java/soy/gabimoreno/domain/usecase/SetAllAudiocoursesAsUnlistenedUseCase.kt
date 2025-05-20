package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import javax.inject.Inject

class SetAllAudiocoursesAsUnlistenedUseCase @Inject constructor(
    private val audioCoursesRepository: AudioCoursesRepository,
) {
    suspend operator fun invoke() {
        audioCoursesRepository.markAllAudioCourseItemsAsUnlistened()
    }
}
