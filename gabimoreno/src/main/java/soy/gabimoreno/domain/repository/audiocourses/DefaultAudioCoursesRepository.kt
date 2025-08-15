package soy.gabimoreno.domain.repository.audiocourses

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import soy.gabimoreno.data.cloud.audiosync.datasource.AudioCoursesCloudDataSource
import soy.gabimoreno.data.local.audiocourse.LocalAudioCoursesDataSource
import soy.gabimoreno.data.local.mapper.toAudioCourseItem
import soy.gabimoreno.data.local.mapper.toPlaylistAudioItem
import soy.gabimoreno.data.remote.datasource.audiocourses.RemoteAudioCoursesDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.domain.repository.premiumaudios.TWELVE_HOURS_IN_MILLIS
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAudioCoursesRepository
    @Inject
    constructor(
        private val cloudDataSource: AudioCoursesCloudDataSource,
        private val localAudioCoursesDataSource: LocalAudioCoursesDataSource,
        private val remoteAudioCoursesDataSource: RemoteAudioCoursesDataSource,
        private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
    ) : AudioCoursesRepository {
        override suspend fun getCourses(
            categories: List<Category>,
            email: String,
            forceRefresh: Boolean,
        ): Either<Throwable, List<AudioCourse>> {
            val shouldRefresh =
                forceRefresh ||
                    refreshPremiumAudiosFromRemoteUseCase(
                        currentTimeInMillis = System.currentTimeMillis(),
                        timeToRefreshInMillis = TWELVE_HOURS_IN_MILLIS,
                    ) ||
                    localAudioCoursesDataSource.isEmpty()

            if (!shouldRefresh) {
                return localAudioCoursesDataSource.getAudioCourses().right()
            }
            return remoteAudioCoursesDataSource
                .getAudioCourses(categories)
                .onRight { remoteAudioCourses ->
                    val mergedCourses =
                        if (email.isNotEmpty()) {
                            mergeWithCloudData(remoteAudioCourses, email)
                        } else {
                            mergeWithLocalData(remoteAudioCourses)
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

        override suspend fun getAudioCourseItem(
            audioCourseItemId: String,
        ): Either<Throwable, AudioCourseItem> =
            localAudioCoursesDataSource
                .getAudioCourseItem(audioCourseItemId)
                ?.toAudioCourseItem()
                ?.right()
                ?: Throwable("AudioCourseItem not found").left()

        override suspend fun getAudioCourseItemById(
            audioCourseItemId: String,
        ): Either<Throwable, PlaylistAudioItem> {
            val audioCourseId = audioCourseItemId.split(AUDIO_COURSE_DELIMITER)[0]
            val position = audioCourseItemId.split(AUDIO_COURSE_DELIMITER)[1].toInt()
            val audioCourse = localAudioCoursesDataSource.getAudioCourseById(audioCourseId).first()

            val audio =
                audioCourse
                    ?.audios
                    ?.firstOrNull { it.id == audioCourseItemId }

            return audio
                ?.toPlaylistAudioItem(audioCourse, position)
                ?.right()
                ?: Throwable("AudioCourse not found").left()
        }

        override suspend fun markAudioCourseItemAsListened(
            audioCourseId: String,
            email: String,
            hasBeenListened: Boolean,
        ) {
            if (email.isNotEmpty()) {
                cloudDataSource.upsertAudioCourseItemFields(
                    email,
                    audioCourseId,
                    mapOf(
                        AUDIO_ID to audioCourseId,
                        HAS_BEEN_LISTENED to hasBeenListened,
                    ),
                )
            }
            localAudioCoursesDataSource.updateHasBeenListened(audioCourseId, hasBeenListened)
        }

        override suspend fun markAllAudioCourseItemsAsUnlistened(email: String) {
            if (email.isNotEmpty()) {
                cloudDataSource.batchUpdateFieldsForAllAudioCoursesItems(
                    email,
                    mapOf(HAS_BEEN_LISTENED to false),
                )
            }
            localAudioCoursesDataSource.markAllAudioCourseItemsAsUnlistened()
        }

        override suspend fun reset() {
            localAudioCoursesDataSource.reset()
        }

        override suspend fun getAllFavoriteAudioCoursesItems(): Either<
            Throwable,
            List<AudioCourseItem>,
        > =
            localAudioCoursesDataSource
                .getAllFavoriteAudioCoursesItems()
                ?.let { audioCourseItems ->
                    audioCourseItems.map { it.toAudioCourseItem() }.right()
                } ?: run {
                Throwable("No favorite audio courses items found").left()
            }

        override suspend fun updateMarkedAsFavorite(
            audioCourseId: String,
            email: String,
            isFavorite: Boolean,
        ) {
            if (email.isNotEmpty()) {
                cloudDataSource.upsertAudioCourseItemFields(
                    email,
                    audioCourseId,
                    mapOf(
                        AUDIO_ID to audioCourseId,
                        MARKED_AS_FAVORITE to isFavorite,
                    ),
                )
            }
            return localAudioCoursesDataSource
                .updateMarkedAsFavorite(audioCourseId, isFavorite)
        }

        private suspend fun mergeWithCloudData(
            remoteAudioCourses: List<AudioCourse>,
            email: String,
        ): List<AudioCourse> {
            val cloudAudioCoursesSnapshot =
                cloudDataSource
                    .getAudioCoursesItems(email)
                    .associateBy { it.id }

            return remoteAudioCourses.map { remoteCourse ->
                val mergedItems =
                    remoteCourse.audios.map { remoteItem ->
                        val cloudItem = cloudAudioCoursesSnapshot[remoteItem.id]
                        remoteItem.copy(
                            hasBeenListened = cloudItem?.hasBeenListened ?: false,
                            markedAsFavorite = cloudItem?.markedAsFavorite ?: false,
                        )
                    }
                remoteCourse.copy(audios = mergedItems)
            }
        }

        private suspend fun mergeWithLocalData(
            remoteAudioCourses: List<AudioCourse>,
        ): List<AudioCourse> {
            val localAudioCoursesSnapshot =
                localAudioCoursesDataSource
                    .getAudioCoursesWithItems()
                    .flatMap { it.audios }
                    .associateBy { it.id }

            return remoteAudioCourses.map { remoteCourse ->
                val mergedItems =
                    remoteCourse.audios.map { remoteItem ->
                        val localItem = localAudioCoursesSnapshot[remoteItem.id]
                        remoteItem.copy(
                            hasBeenListened = localItem?.hasBeenListened ?: false,
                            markedAsFavorite = localItem?.markedAsFavorite ?: false,
                        )
                    }
                remoteCourse.copy(audios = mergedItems)
            }
        }
    }

private const val AUDIO_ID = "id"
private const val AUDIO_COURSE_DELIMITER = "-"
private const val HAS_BEEN_LISTENED = "hasBeenListened"
private const val MARKED_AS_FAVORITE = "markedAsFavorite"
