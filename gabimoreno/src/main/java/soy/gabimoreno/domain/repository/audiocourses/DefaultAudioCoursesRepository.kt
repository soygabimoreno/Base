package soy.gabimoreno.domain.repository.audiocourses

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import soy.gabimoreno.data.local.audiocourse.LocalAudioCoursesDataSource
import soy.gabimoreno.data.local.mapper.toAudioCourseItem
import soy.gabimoreno.data.remote.datasource.audiocourses.RemoteAudioCoursesDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.domain.repository.premiumaudios.TWELVE_HOURS_IN_MILLIS
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAudioCoursesRepository @Inject constructor(
    private val localAudioCoursesDataSource: LocalAudioCoursesDataSource,
    private val remoteAudioCoursesDataSource: RemoteAudioCoursesDataSource,
    private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
) : AudioCoursesRepository {
    override suspend fun getCourses(
        categories: List<Category>,
        forceRefresh: Boolean
    ): Either<Throwable, List<AudioCourse>> {
        val shouldRefresh = forceRefresh ||
            refreshPremiumAudiosFromRemoteUseCase(
                currentTimeInMillis = System.currentTimeMillis(),
                timeToRefreshInMillis = TWELVE_HOURS_IN_MILLIS
            ) ||
            localAudioCoursesDataSource.isEmpty()

        if (!shouldRefresh) {
            return localAudioCoursesDataSource.getAudioCourses().right()
        }

        val localAudioCoursesSnapshot = localAudioCoursesDataSource.getAudioCoursesWithItems()
            .flatMap { it.audios }
            .associateBy { it.id }

        return remoteAudioCoursesDataSource.getAudioCourses(categories)
            .onRight { remoteAudioCourses ->
                val mergedCourses = remoteAudioCourses.map { remoteCourse ->
                    val mergedItems = remoteCourse.audios.map { remoteItem ->
                        val localItem = localAudioCoursesSnapshot[remoteItem.id]
                        remoteItem.copy(hasBeenListened = localItem?.hasBeenListened ?: false)
                    }
                    remoteCourse.copy(audios = mergedItems)
                }

                localAudioCoursesDataSource.saveAudioCourses(mergedCourses)
            }
    }

    override suspend fun getCourseById(idCourse: String): Either<Throwable, Flow<AudioCourse>> {
        val audioCourseFlow = localAudioCoursesDataSource.getAudioCourseById(idCourse)
        val first = audioCourseFlow.first()
        return if (first != null) {
            audioCourseFlow.filterNotNull().right()
        } else {
            Throwable("AudioCourse not found").left()
        }
    }

    override suspend fun getAudioCourseItem(audioCourseItemId: String): Either<Throwable, AudioCourseItem> {
        localAudioCoursesDataSource.getAudioCourseItem(audioCourseItemId)
            ?.let { audioCourseItemDbModel ->
                return audioCourseItemDbModel.toAudioCourseItem().right()
            } ?: return Throwable("AudioCourseItem not found").left()
    }

    override suspend fun markAudioCourseItemAsListened(id: String, hasBeenListened: Boolean) {
        localAudioCoursesDataSource.updateHasBeenListened(id, hasBeenListened)
    }

    override suspend fun markAllAudioCourseItemsAsUnlistened() {
        localAudioCoursesDataSource.markAllAudioCourseItemsAsUnlistened()
    }

    override suspend fun reset() {
        localAudioCoursesDataSource.reset()
    }

    override suspend fun getAllFavoriteAudioCoursesItems(): Either<Throwable, List<AudioCourseItem>> {
        return localAudioCoursesDataSource.getAllFavoriteAudioCoursesItems()
            ?.let { audioCourseItems ->
                audioCourseItems.map { it.toAudioCourseItem() }.right()
            } ?: run {
            Throwable("No favorite audio courses items found").left()
        }
    }

    override suspend fun updateMarkedAsFavorite(id: String, isFavorite: Boolean) {
        return localAudioCoursesDataSource.updateMarkedAsFavorite(id, isFavorite)
    }
}
