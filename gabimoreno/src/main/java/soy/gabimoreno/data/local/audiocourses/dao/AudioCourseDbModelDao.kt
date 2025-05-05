package soy.gabimoreno.data.local.audiocourses.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import soy.gabimoreno.data.local.audiocourses.model.AudioCourseDbModel

@Dao
interface AudioCourseDbModelDao {
    @Query("SELECT * FROM AudioCourseDbModel")
    fun getAudioCourseDbModels(): List<AudioCourseDbModel>

    @Query("SELECT * FROM AudioCourseDbModel WHERE id = :id")
    fun getAudioCourseDbModelById(id: String): AudioCourseDbModel?

    @Upsert
    fun upsertAudioCourseDbModels(audioCourseDbModels: List<AudioCourseDbModel>)

    @Query("SELECT COUNT(*) FROM AudioCourseDbModel")
    fun count(): Int

    @Query("DELETE FROM AudioCourseDbModel")
    fun deleteAllAudioCourseDbModels()
}
