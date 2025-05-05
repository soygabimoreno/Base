package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import javax.inject.Inject

class GetAudioCourseByIdUseCase @Inject constructor(
    private val audioCoursesRepository: AudioCoursesRepository
) {
    suspend operator fun invoke(audioCourseId: String): Either<Throwable, AudioCourse> {
        return audioCoursesRepository.getCourseById(audioCourseId)
    }
}
