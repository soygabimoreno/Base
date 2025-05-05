package soy.gabimoreno.data.local.audiocourses.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import soy.gabimoreno.data.local.audiocourses.model.AudioCourseItemDbModel

@Dao
interface AudioCourseItemDbModelDao {
    @Query("SELECT * FROM AudioCourseItems WHERE idAudioCourse = :idAudioCourse")
    fun getAudioCourseItemsByAudioCourseId(idAudioCourse: String): List<AudioCourseItemDbModel>

    @Upsert()
    fun upsertAudioCourseItems(audioCourseItems: List<AudioCourseItemDbModel>)

    @Query("SELECT COUNT(*) FROM AudioCourseItems WHERE idAudioCourse = :idAudioCourse")
    fun countAudioCourseItemsByAudioCourseId(idAudioCourse: String): Int

    @Query("DELETE FROM AudioCourseItems")
    fun deleteAllAudioCourseItems()

    @Query("DELETE FROM AudioCourseItems WHERE idAudioCourse = :idAudioCourse")
    fun deleteAudioCourseItemsByAudioCourseId(idAudioCourse: String)
}
