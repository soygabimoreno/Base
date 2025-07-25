package soy.gabimoreno.data.local.audiocourse.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseDbModel
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseItemDbModel
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseWithItems
import soy.gabimoreno.data.local.mapper.toAudioCourseDbModelMapper
import soy.gabimoreno.data.remote.mapper.toAudioCourseItemDbModelMapper
import soy.gabimoreno.domain.model.content.AudioCourse

@Dao
interface AudioCourseTransactionDao {
    @Transaction
    @Query("SELECT * FROM AudioCourseDbModel WHERE id = :id")
    fun getAudioCourseWithItems(id: String): Flow<AudioCourseWithItems?>

    @Transaction
    @Query("SELECT * FROM AudioCourseDbModel")
    suspend fun getAudioCoursesWithItems(): List<AudioCourseWithItems>

    @Transaction
    suspend fun upsertAudioCoursesWithItems(audioCourses: List<AudioCourse>) {
        val courses = audioCourses.map { it.toAudioCourseDbModelMapper() }
        val items =
            audioCourses.flatMap { course ->
                course.audios.map { it.toAudioCourseItemDbModelMapper(course.id) }
            }

        upsertCourses(courses)
        upsertItems(items)
    }

    @Upsert
    fun upsertCourses(courses: List<AudioCourseDbModel>)

    @Upsert
    fun upsertItems(items: List<AudioCourseItemDbModel>)
}
