package soy.gabimoreno.domain.repository.playlist

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.domain.model.content.Playlist

interface PlaylistRepository {
    suspend fun isEmpty(): Either<Throwable, Boolean>
    suspend fun insertPlaylist(name: String, description: String): Either<Throwable, Playlist>
    suspend fun savePlaylist(playlist: Playlist): Either<Throwable, Unit>
    suspend fun getAllPlaylists(): Either<Throwable, List<Playlist>>
    fun getPlaylistById(idPlaylist: Int): Either<Throwable, Flow<Playlist?>>
    suspend fun resetPlaylistById(idPlaylist: Int): Either<Throwable, Unit>
    suspend fun deleteAllPlaylists(): Either<Throwable, Unit>
}
