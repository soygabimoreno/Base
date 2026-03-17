package soy.gabimoreno.data.local.podcast.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.local.podcast.model.SeniorAudioDbModel

@Dao
interface SeniorDbModelDao {
    @Query("SELECT * FROM SeniorAudioDbModel ORDER BY pubDateMillis DESC")
    fun getSeniorDbModels(): Flow<List<SeniorAudioDbModel>>

    @Query("SELECT * FROM SeniorAudioDbModel WHERE id = :id")
    fun getSeniorDbModelById(id: String): SeniorAudioDbModel?

    @Query("SELECT * FROM SeniorAudioDbModel WHERE id IN (:ids)")
    suspend fun getSeniorDbModelByIds(ids: Set<String>): List<SeniorAudioDbModel>

    @Query("SELECT COUNT(id) FROM SeniorAudioDbModel")
    fun count(): Int

    @Upsert
    fun upsertSeniorDbModels(seniorAudioDbModels: List<SeniorAudioDbModel>)

    @Query("DELETE FROM SeniorAudioDbModel")
    fun deleteAllSeniorDbModels()
}
