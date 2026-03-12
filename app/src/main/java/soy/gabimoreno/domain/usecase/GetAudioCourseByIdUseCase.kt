package soy.gabimoreno.domain.usecase

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.AudioCourseRepository
import javax.inject.Inject

class GetAudioCourseByIdUseCase
    @Inject
    constructor(
        private val audioCourseRepository: AudioCourseRepository,
    ) {
        suspend operator fun invoke(audioCourseId: String): Either<Throwable, Flow<AudioCourse>> =
            audioCourseRepository.getAudioCourseById(audioCourseId)
    }
