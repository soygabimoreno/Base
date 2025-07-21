package soy.gabimoreno.domain.repository.audiocourses

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.content.AudioCourseItem

interface AudioCoursesRepository {
    suspend fun getCourses(
        categories: List<Category>,
        email: String,
        forceRefresh: Boolean = false,
    ): Either<Throwable, List<AudioCourse>>
    suspend fun getCourseById(idCourse: String): Either<Throwable, Flow<AudioCourse>>
    suspend fun getAudioCourseItem(audioCourseItemId: String): Either<Throwable, AudioCourseItem>
    suspend fun getAllFavoriteAudioCoursesItems(): Either<Throwable, List<AudioCourseItem>>
    suspend fun markAudioCourseItemAsListened(
        audioCourseId: String,
        email: String,
        hasBeenListened: Boolean,
    )
    suspend fun markAllAudioCourseItemsAsUnlistened(email: String)
    suspend fun reset()
    suspend fun updateMarkedAsFavorite(
        audioCourseId: String,
        email: String,
        isFavorite: Boolean,
    )
}
