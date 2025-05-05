package soy.gabimoreno.data.local.audiocourses

import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.GabiMorenoDatabase
import soy.gabimoreno.data.local.mapper.toAudioCourse
import soy.gabimoreno.data.local.mapper.toAudioCourseMapper
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.content.AudioCourse
import javax.inject.Inject

class LocalAudioCoursesDataSource @Inject constructor(
    gabiMorenoDatabase: GabiMorenoDatabase,
    @IO private val dispatcher: CoroutineDispatcher,
) {

    @VisibleForTesting
    val audioCourseDbModelDao = gabiMorenoDatabase.audioCourseDbModelDao()

    @VisibleForTesting
    val audioCourseItemDbModelDao = gabiMorenoDatabase.audioCourseItemDbModelDao()

    @VisibleForTesting
    val audioCourseTransactionDao = gabiMorenoDatabase.audioCourseTransactionDao()

    suspend fun isEmpty(): Boolean = withContext(dispatcher) {
        audioCourseDbModelDao.count() <= 0
    }

    suspend fun saveAudioCourses(audioCourses: List<AudioCourse>) = withContext(dispatcher) {
        audioCourseTransactionDao.upsertAudioCoursesWithItems(audioCourses)
    }

    suspend fun getAudioCourses(): List<AudioCourse> = withContext(dispatcher) {
        audioCourseDbModelDao.getAudioCourseDbModels().map {
            it.toAudioCourseMapper()
        }
    }

    suspend fun getAudioCourseById(id: String): AudioCourse? = withContext(dispatcher) {
        audioCourseTransactionDao.getAudioCoursesWithItems(id)?.toAudioCourse()
    }

    suspend fun reset() = withContext(dispatcher) {
        audioCourseDbModelDao.deleteAllAudioCourseDbModels()
    }
}
