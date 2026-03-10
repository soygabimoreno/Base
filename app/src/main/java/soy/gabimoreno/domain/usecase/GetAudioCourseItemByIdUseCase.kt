package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import javax.inject.Inject

class GetAudioCourseItemByIdUseCase
    @Inject
    constructor(
        private val audioCoursesRepository: AudioCoursesRepository,
    ) {
        suspend operator fun invoke(audioCourseItemId: String): Either<Throwable, AudioCourseItem> =
            audioCoursesRepository.getAudioCourseItem(audioCourseItemId)
    }
