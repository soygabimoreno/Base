package soy.gabimoreno.data.local.audiocourse.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseItemDbModel

@Dao
interface AudioCourseItemDbModelDao {
    @Query("SELECT * FROM AudioCourseItems WHERE idAudioCourse = :audioCourseId")
    fun getAudioCourseItemsByAudioCourseId(audioCourseId: String): List<AudioCourseItemDbModel>

    @Query("SELECT * FROM AudioCourseItems WHERE id = :idAudioCourseItem")
    fun getAudioCourseItemById(idAudioCourseItem: String): AudioCourseItemDbModel?

    @Query("SELECT * FROM AudioCourseItems WHERE id IN (:ids)")
    suspend fun getAudioCourseItemsByIds(ids: Set<String>): List<AudioCourseItemDbModel>

    @Query("SELECT * FROM AudioCourseItems WHERE markedAsFavorite = 1")
    fun getAllFavoriteAudioCoursesItems(): List<AudioCourseItemDbModel>

    @Upsert()
    fun upsertAudioCourseItems(audioCourseItems: List<AudioCourseItemDbModel>)

    @Query("UPDATE AudioCourseItems SET markedAsFavorite = :markedAsFavorite WHERE id = :audioCourseId")
    fun updateMarkedAsFavorite(audioCourseId: String, markedAsFavorite: Boolean)

    @Query("UPDATE AudioCourseItems SET hasBeenListened = :hasBeenListened WHERE id = :audioCourseId")
    fun updateHasBeenListened(audioCourseId: String, hasBeenListened: Boolean)

    @Query("UPDATE AudioCourseItems SET hasBeenListened = 0")
    fun markAllAudioCourseItemsAsUnlistened()

    @Query("SELECT COUNT(*) FROM AudioCourseItems WHERE idAudioCourse = :audioCourseId")
    fun countAudioCourseItemsByAudioCourseId(audioCourseId: String): Int

    @Query("DELETE FROM AudioCourseItems")
    fun deleteAllAudioCourseItems()

    @Query("DELETE FROM AudioCourseItems WHERE idAudioCourse = :audioCourseId")
    fun deleteAudioCourseItemsByAudioCourseId(audioCourseId: String)
}
