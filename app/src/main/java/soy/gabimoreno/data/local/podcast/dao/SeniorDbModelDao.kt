package soy.gabimoreno.data.local.podcast.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.local.podcast.model.SeniorDbModel

@Dao
interface SeniorDbModelDao {
    @Query("SELECT * FROM SeniorDbModel ORDER BY pubDateMillis DESC")
    fun getSeniorDbModels(): Flow<List<SeniorDbModel>>

    @Query("SELECT * FROM SeniorDbModel WHERE id = :id")
    fun getSeniorDbModelById(id: String): SeniorDbModel?

    @Query("SELECT * FROM SeniorDbModel WHERE id IN (:ids)")
    suspend fun getSeniorDbModelByIds(ids: Set<String>): List<SeniorDbModel>

    @Query("SELECT COUNT(id) FROM SeniorDbModel")
    fun count(): Int

    @Upsert
    fun upsertSeniorDbModels(seniorDbModels: List<SeniorDbModel>)

    @Query("DELETE FROM SeniorDbModel")
    fun deleteAllSeniorDbModels()
}
