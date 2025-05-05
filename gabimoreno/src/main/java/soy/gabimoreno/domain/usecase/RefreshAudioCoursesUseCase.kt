package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCoursesRepository
import javax.inject.Inject

class RefreshAudioCoursesUseCase @Inject constructor(
    private val audioCoursesRepository: DefaultAudioCoursesRepository,
) {
    suspend operator fun invoke() {
        return audioCoursesRepository.reset()
    }
}
