package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import javax.inject.Inject

class GetAudioCoursesUseCase @Inject constructor(
    private val audioCoursesRepository: AudioCoursesRepository
) {
    suspend operator fun invoke(
        categories: List<Category>,
        forceRefresh: Boolean = false
    ): Either<Throwable, List<AudioCourse>> {
        return audioCoursesRepository.getCourses(categories, forceRefresh)
    }
}
