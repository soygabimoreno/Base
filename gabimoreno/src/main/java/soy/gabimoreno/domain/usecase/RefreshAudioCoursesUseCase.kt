package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCoursesRepository
import soy.gabimoreno.domain.repository.premiumaudios.DefaultPremiumAudiosRepository
import javax.inject.Inject

class RefreshAudioCoursesUseCase @Inject constructor(
    private val audioCoursesRepository: DefaultAudioCoursesRepository,
) {

    suspend operator fun invoke() {
        return audioCoursesRepository.reset()
    }
}
