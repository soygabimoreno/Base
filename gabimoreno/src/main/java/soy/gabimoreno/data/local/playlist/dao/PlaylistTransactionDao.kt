package soy.gabimoreno.data.local.playlist.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.local.mapper.toPlaylistDbModel
import soy.gabimoreno.data.local.mapper.toPlaylistItemDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistWithItems
import soy.gabimoreno.domain.model.content.Playlist

@Dao
interface PlaylistTransactionDao {

    @Transaction
    @Query("SELECT * FROM PlaylistDbModel WHERE id = :id")
    fun getPlaylistWithItemsById(id: Int): Flow<PlaylistWithItems?>

    @Transaction
    @Query("SELECT * FROM PlaylistDbModel ORDER BY position ASC")
    suspend fun getPlaylistsWithItems(): List<PlaylistWithItems>

    @Transaction
    suspend fun upsertPlaylistsWithItems(
        playlists: List<Playlist>
    ) {
        val courses = playlists.map { it.toPlaylistDbModel() }
        val items = playlists.flatMap { playlist ->
            playlist.items.map { it.toPlaylistItemDbModel(playlist.id) }
        }
        upsertPlaylists(courses)
        upsertItems(items)
    }

    @Upsert
    fun upsertPlaylists(playlists: List<PlaylistDbModel>)

    @Upsert
    fun upsertItems(items: List<PlaylistItemsDbModel>)

    @Query("SELECT playlistId FROM PlaylistItemsDbModel WHERE id = :playlistItemId")
    suspend fun getPlaylistIdsByItemId(playlistItemId: String): List<Int>
}
