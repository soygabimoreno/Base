package soy.gabimoreno.data.local.playlist.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel

@Dao
interface PlaylistItemDbModelDao {
    @Query("SELECT * FROM PlaylistItemsDbModel WHERE idPlaylist = :playlistId")
    fun getPlaylistItemsDbModelByPlaylistId(playlistId: Int): List<PlaylistItemsDbModel>

    @Query("SELECT * FROM PlaylistItemsDbModel WHERE idPlaylist = :playlistId ORDER BY position ASC")
    fun getPlaylistItemsDbModelOrdered(playlistId: Int): List<PlaylistItemsDbModel>

    @Upsert()
    fun upsertPlaylistItemsDbModel(playlistItems: List<PlaylistItemsDbModel>)

    @Query("SELECT COUNT(*) FROM PlaylistItemsDbModel WHERE idPlaylist = :playlistId")
    fun getPlaylistItemsCountByPlaylistId(playlistId: Int): Int

    @Query("DELETE FROM PlaylistItemsDbModel WHERE idPlaylist = :playlistId")
    fun deletePlaylistItemsDbModelByPlaylistId(playlistId: Int)

    @Query("DELETE FROM PlaylistItemsDbModel")
    fun deleteAllPlaylistItemsDbModel()
}
