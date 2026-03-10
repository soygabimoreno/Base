package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.Either
import kotlinx.coroutines.flow.first
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class GetAudioCoursesUseCase
    @Inject
    constructor(
        private val audioCoursesRepository: AudioCoursesRepository,
        private val context: Context,
    ) {
        suspend operator fun invoke(
            categories: List<Category>,
            forceRefresh: Boolean = false,
        ): Either<Throwable, List<AudioCourse>> {
            val email = context.getEmail().first()
            return audioCoursesRepository.getCourses(
                categories = categories,
                email = email,
                forceRefresh = forceRefresh,
            )
        }
    }
