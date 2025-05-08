package soy.gabimoreno.domain.repository.audiocourses

import arrow.core.Either
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse

interface AudioCoursesRepository {
    suspend fun getCourses(categories: List<Category>): Either<Throwable, List<AudioCourse>>
    suspend fun getCourseById(idCourse: String): Either<Throwable, AudioCourse>
    suspend fun reset()
}
