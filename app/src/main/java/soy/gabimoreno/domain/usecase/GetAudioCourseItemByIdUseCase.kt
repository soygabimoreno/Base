package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.domain.repository.audiocourse.AudioCourseRepository
import javax.inject.Inject

class GetAudioCourseItemByIdUseCase
    @Inject
    constructor(
        private val audioCourseRepository: AudioCourseRepository,
    ) {
        suspend operator fun invoke(audioCourseItemId: String): Either<Throwable, AudioCourseItem> =
            audioCourseRepository.getAudioCourseItem(audioCourseItemId)
    }
