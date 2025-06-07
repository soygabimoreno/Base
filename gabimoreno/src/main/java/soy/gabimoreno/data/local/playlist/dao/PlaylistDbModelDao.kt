package soy.gabimoreno.data.local.playlist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel

@Dao
interface PlaylistDbModelDao {
    @Query("SELECT * FROM PlaylistDbModel ORDER BY position ASC")
    fun getPlaylistDbModels(): List<PlaylistDbModel>

    @Query("SELECT * FROM PlaylistDbModel WHERE id = :id")
    fun getPlaylistDbModelById(id: String): PlaylistDbModel?

    @Query("SELECT COUNT(*) FROM PlaylistDbModel")
    fun getTotalPlaylists(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistDbModel(playlistDbModel: PlaylistDbModel): Long

    @Upsert
    fun upsertPlaylistDbModels(playlistDbModels: List<PlaylistDbModel>)

    @Query("DELETE FROM PlaylistDbModel WHERE id = :id")
    fun deletePlaylistDbModelById(id: String)

    @Query("DELETE FROM PlaylistDbModel")
    fun deleteAllPlaylistDbModels()
}
