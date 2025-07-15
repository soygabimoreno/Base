package soy.gabimoreno.domain.repository.playlist

import arrow.core.Either
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistAudioItem

interface PlaylistRepository {
    suspend fun isEmpty(): Either<Throwable, Boolean>
    suspend fun insertPlaylist(
        name: String,
        description: String,
        email: String
    ): Either<Throwable, Playlist>

    suspend fun savePlaylist(playlist: Playlist, email: String): Either<Throwable, Unit>
    suspend fun getAllPlaylists(email: String): Either<Throwable, List<Playlist>>
    suspend fun getPlaylistById(idPlaylist: Int): Either<Throwable, Playlist?>
    suspend fun getPlaylistIdsByItemId(audioItemId: String): Either<Throwable, List<Int>>
    suspend fun upsertPlaylists(playlists: List<Playlist>, email: String): Either<Throwable, Unit>
    suspend fun upsertPlaylistItems(
        audioId: String,
        playlistIds: List<Int>,
        email: String,
    ): Either<Throwable, List<Long>>

    suspend fun updatePlaylistItems(
        playlistId: Int,
        playlistItems: List<PlaylistAudioItem>,
        email: String,
    ): Either<Throwable, Unit>

    suspend fun resetPlaylistById(idPlaylist: Int, email: String): Either<Throwable, Unit>
    suspend fun deletePlaylistItemById(
        audioItemId: String,
        playlistId: Int,
        email: String,
    ): Either<Throwable, Unit>

    suspend fun deletePlaylistById(playlistId: Int, email: String): Either<Throwable, Unit>
    suspend fun deleteAllPlaylists(email: String): Either<Throwable, Unit>
}
