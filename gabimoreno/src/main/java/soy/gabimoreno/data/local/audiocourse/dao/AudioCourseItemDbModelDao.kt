package soy.gabimoreno.data.local.audiocourse.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseItemDbModel

@Dao
interface AudioCourseItemDbModelDao {
    @Query("SELECT * FROM AudioCourseItems WHERE idAudioCourse = :audioCourseId")
    fun getAudioCourseItemsByAudioCourseId(audioCourseId: String): List<AudioCourseItemDbModel>

    @Upsert()
    fun upsertAudioCourseItems(audioCourseItems: List<AudioCourseItemDbModel>)

    @Query("UPDATE audioCourseItems SET hasBeenListened = :hasBeenListened WHERE id = :id")
    fun updateHasBeenListened(id: String, hasBeenListened: Boolean)

    @Query("SELECT COUNT(*) FROM AudioCourseItems WHERE idAudioCourse = :audioCourseId")
    fun countAudioCourseItemsByAudioCourseId(audioCourseId: String): Int

    @Query("DELETE FROM AudioCourseItems")
    fun deleteAllAudioCourseItems()

    @Query("DELETE FROM AudioCourseItems WHERE idAudioCourse = :audioCourseId")
    fun deleteAudioCourseItemsByAudioCourseId(audioCourseId: String)
}
