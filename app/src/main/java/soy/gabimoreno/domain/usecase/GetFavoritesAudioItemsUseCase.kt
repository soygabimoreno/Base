package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import javax.inject.Inject

class GetFavoritesAudioItemsUseCase
    @Inject
    constructor(
        private val audioCoursesRepository: AudioCoursesRepository,
    ) {
        suspend operator fun invoke(): Either<Throwable, List<AudioCourseItem>> =
            audioCoursesRepository.getAllFavoriteAudioCoursesItems()
    }
