@file:Suppress("TooManyFunctions")

package soy.gabimoreno.data.local.audiocourse

import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.ApplicationDatabase
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseItemDbModel
import soy.gabimoreno.data.local.mapper.toAudioCourse
import soy.gabimoreno.data.local.mapper.toAudioCourseMapper
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.content.AudioCourse
import javax.inject.Inject

class LocalAudioCoursesDataSource
    @Inject
    constructor(
        gabiMorenoDatabase: ApplicationDatabase,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) {
        @VisibleForTesting
        val audioCourseDbModelDao = gabiMorenoDatabase.audioCourseDbModelDao()

        @VisibleForTesting
        val audioCourseItemDbModelDao = gabiMorenoDatabase.audioCourseItemDbModelDao()

        @VisibleForTesting
        val audioCourseTransactionDao = gabiMorenoDatabase.audioCourseTransactionDao()

        suspend fun isEmpty(): Boolean =
            withContext(dispatcher) {
                audioCourseDbModelDao.count() <= 0
            }

        suspend fun saveAudioCourses(audioCourses: List<AudioCourse>) =
            withContext(dispatcher) {
                audioCourseTransactionDao.upsertAudioCoursesWithItems(audioCourses)
            }

        suspend fun updateHasBeenListened(
            id: String,
            hasBeenListened: Boolean,
        ) = withContext(dispatcher) {
            audioCourseItemDbModelDao.updateHasBeenListened(id, hasBeenListened)
        }

        suspend fun getAudioCourses(): List<AudioCourse> =
            withContext(dispatcher) {
                audioCourseDbModelDao.getAudioCourseDbModels().map {
                    it.toAudioCourseMapper()
                }
            }

        suspend fun getAudioCoursesWithItems(): List<AudioCourse> =
            withContext(dispatcher) {
                audioCourseTransactionDao.getAudioCoursesWithItems().map {
                    it.toAudioCourse()
                }
            }

        fun getAudioCourseById(id: String): Flow<AudioCourse?> =
            audioCourseTransactionDao
                .getAudioCourseWithItems(id)
                .map { it?.toAudioCourse() }

        suspend fun getAudioCourseItem(audioCourseItemId: String): AudioCourseItemDbModel? =
            withContext(dispatcher) {
                audioCourseItemDbModelDao.getAudioCourseItemById(audioCourseItemId)
            }

        suspend fun getAllFavoriteAudioCoursesItems(): List<AudioCourseItemDbModel> =
            withContext(dispatcher) {
                audioCourseItemDbModelDao.getAllFavoriteAudioCoursesItems()
            }

        suspend fun updateMarkedAsFavorite(
            audioItemId: String,
            markedAsFavorite: Boolean,
        ) = withContext(dispatcher) {
            audioCourseItemDbModelDao.updateMarkedAsFavorite(
                audioItemId,
                markedAsFavorite = markedAsFavorite,
            )
        }

        suspend fun markAllAudioCourseItemsAsUnlistened() =
            withContext(dispatcher) {
                audioCourseItemDbModelDao.markAllAudioCourseItemsAsUnlistened()
            }

        suspend fun reset() =
            withContext(dispatcher) {
                audioCourseDbModelDao.deleteAllAudioCourseDbModels()
            }
    }
