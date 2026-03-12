package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCourseRepository
import javax.inject.Inject

class RefreshAudioCoursesUseCase
    @Inject
    constructor(
        private val audioCoursesRepository: DefaultAudioCourseRepository,
    ) {
        suspend operator fun invoke() = audioCoursesRepository.reset()
    }
