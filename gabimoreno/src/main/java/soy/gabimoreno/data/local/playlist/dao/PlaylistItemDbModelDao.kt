package soy.gabimoreno.data.local.playlist.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel

@Dao
interface PlaylistItemDbModelDao {
    @Query("SELECT * FROM PlaylistItemsDbModel WHERE playlistId = :playlistId")
    fun getPlaylistItemsDbModelByPlaylistId(playlistId: Int): List<PlaylistItemsDbModel>

    @Query("SELECT * FROM PlaylistItemsDbModel WHERE playlistId = :playlistId ORDER BY position ASC")
    fun getPlaylistItemsDbModelOrdered(playlistId: Int): List<PlaylistItemsDbModel>

    @Upsert()
    fun upsertPlaylistItemsDbModel(playlistItems: List<PlaylistItemsDbModel>): List<Long>

    @Query("SELECT COUNT(*) FROM PlaylistItemsDbModel WHERE playlistId = :playlistId")
    fun getPlaylistItemsCountByPlaylistId(playlistId: Int): Int

    @Query("SELECT COUNT(*) FROM PlaylistItemsDbModel")
    fun getTotalPlaylistItems(): Int

    @Query("DELETE FROM PlaylistItemsDbModel WHERE playlistId = :playlistId")
    fun deletePlaylistItemsDbModelByPlaylistId(playlistId: Int)

    @Query("DELETE FROM PlaylistItemsDbModel")
    fun deleteAllPlaylistItemsDbModel()
}
