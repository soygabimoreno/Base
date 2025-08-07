package soy.gabimoreno.data.local.podcast.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.local.podcast.model.PodcastDbModel

@Dao
interface PodcastDbModelDao {
    @Query("SELECT * FROM PodcastDbModel")
    fun getPodcastDbModels(): Flow<List<PodcastDbModel>>

    @Query("SELECT * FROM PodcastDbModel WHERE id = :id")
    fun getPodcastDbModelById(id: String): PodcastDbModel?

    @Query("SELECT COUNT(id) FROM PodcastDbModel")
    fun count(): Int

    @Upsert
    fun upsertPodcastDbModels(podcastDbModels: List<PodcastDbModel>)

    @Query("DELETE FROM PodcastDbModel")
    fun deleteAllPodcastDbModels()

    @Query("UPDATE PodcastDbModel SET hasBeenListened = :hasBeenListened WHERE id = :id")
    fun updateHasBeenListened(
        id: String,
        hasBeenListened: Boolean,
    )

    @Query("UPDATE PodcastDbModel SET markedAsFavorite = :markedAsFavorite WHERE id = :id")
    fun updateMarkedAsFavorite(
        id: String,
        markedAsFavorite: Boolean,
    )
}
