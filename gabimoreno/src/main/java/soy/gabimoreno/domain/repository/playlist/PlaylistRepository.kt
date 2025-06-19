package soy.gabimoreno.domain.repository.playlist

import arrow.core.Either
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistAudioItem

interface PlaylistRepository {
    suspend fun isEmpty(): Either<Throwable, Boolean>
    suspend fun insertPlaylist(name: String, description: String): Either<Throwable, Playlist>
    suspend fun savePlaylist(playlist: Playlist): Either<Throwable, Unit>
    suspend fun getAllPlaylists(): Either<Throwable, List<Playlist>>
    suspend fun getPlaylistById(idPlaylist: Int): Either<Throwable, Playlist?>
    suspend fun getPlaylistIdsByItemId(audioItemId: String): Either<Throwable, List<Int>>
    suspend fun upsertPlaylists(playlists: List<Playlist>): Either<Throwable, Unit>
    suspend fun upsertPlaylistItems(
        audioId: String,
        playlistIds: List<Int>
    ): Either<Throwable, List<Long>>
    suspend fun updatePlaylistItems(
        playlistId: Int,
        playlistItems: List<PlaylistAudioItem>
    ): Either<Throwable, Unit>
    suspend fun resetPlaylistById(idPlaylist: Int): Either<Throwable, Unit>
    suspend fun deletePlaylistItemById(
        audioItemId: String,
        playlistId: Int
    ): Either<Throwable, Unit>
    suspend fun deletePlaylistById(playlistId: Int): Either<Throwable, Unit>
    suspend fun deleteAllPlaylists(): Either<Throwable, Unit>
}
