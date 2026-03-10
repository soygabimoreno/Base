@file:Suppress("TooManyFunctions")

package soy.gabimoreno.data.local.premiumaudio

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PremiumAudioDbModelDao {
    @Query("SELECT * FROM PremiumAudioDbModel")
    fun getPremiumAudioDbModels(): List<PremiumAudioDbModel>

    @Query("SELECT * FROM PremiumAudioDbModel")
    fun getPremiumAudioDbModelsPagingSource(): PagingSource<Int, PremiumAudioDbModel>

    @Query("SELECT * FROM PremiumAudioDbModel WHERE id = :id")
    fun getPremiumAudioDbModelById(id: String): PremiumAudioDbModel?

    @Query("SELECT * FROM PremiumAudioDbModel WHERE id IN (:ids)")
    suspend fun getPremiumAudiosByIds(ids: Set<String>): List<PremiumAudioDbModel>

    @Query("SELECT * FROM PremiumAudioDbModel WHERE markedAsFavorite = 1")
    fun getAllFavoriteAudioPremiumAudioDbModels(): List<PremiumAudioDbModel>

    @Query("SELECT COUNT(id) FROM PremiumAudioDbModel")
    fun count(): Int

    @Upsert()
    fun upsertPremiumAudioDbModels(premiumAudioDbModels: List<PremiumAudioDbModel>)

    @Query("UPDATE PremiumAudioDbModel SET markedAsFavorite = :markedAsFavorite WHERE id = :id")
    fun updateMarkedAsFavorite(
        id: String,
        markedAsFavorite: Boolean,
    )

    @Query("UPDATE PremiumAudioDbModel SET hasBeenListened = 0")
    fun markAllPremiumAudiosAsUnlistened()

    @Query("UPDATE PremiumAudioDbModel SET hasBeenListened = :hasBeenListened WHERE id = :id")
    fun updateHasBeenListened(
        id: String,
        hasBeenListened: Boolean,
    )

    @Query("DELETE FROM PremiumAudioDbModel")
    fun deleteAllPremiumAudioDbModels()
}
