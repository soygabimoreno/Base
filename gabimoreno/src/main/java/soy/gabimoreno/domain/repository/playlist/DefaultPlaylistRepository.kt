package soy.gabimoreno.domain.repository.playlist

import arrow.core.Either
import kotlinx.coroutines.flow.first
import soy.gabimoreno.data.local.mapper.toPlaylistItemDbModel
import soy.gabimoreno.data.local.playlist.LocalPlaylistDataSource
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPlaylistRepository @Inject constructor(
    private val localPlaylistDataSource: LocalPlaylistDataSource,
) : PlaylistRepository {
    override suspend fun isEmpty(): Either<Throwable, Boolean> {
        return localPlaylistDataSource.isEmpty().let { Either.Right(it) }
    }

    override suspend fun insertPlaylist(
        name: String,
        description: String
    ): Either<Throwable, Playlist> {
        return localPlaylistDataSource.insertPlaylist(name, description).let { Either.Right(it) }
    }

    override suspend fun savePlaylist(playlist: Playlist): Either<Throwable, Unit> {
        return localPlaylistDataSource.savePlaylist(listOf(playlist)).let { Either.Right(Unit) }
    }

    override suspend fun getAllPlaylists(): Either<Throwable, List<Playlist>> {
        return localPlaylistDataSource.getAllPlaylists().let { Either.Right(it) }
    }

    override suspend fun getPlaylistById(idPlaylist: Int): Either<Throwable, Playlist?> {
        return localPlaylistDataSource.getPlaylistById(idPlaylist).let { Either.Right(it.first()) }
    }

    override suspend fun getPlaylistIdsByItemId(audioItemId: String): Either<Throwable, List<Int>> {
        return localPlaylistDataSource.getPlaylistIdsByItemId(audioItemId)
            .let { Either.Right(it) }
    }

    override suspend fun upsertPlaylists(playlists: List<Playlist>): Either<Throwable, Unit> {
        return localPlaylistDataSource.upsertPlaylistDbModels(playlists)
            .let { Either.Right(Unit) }
    }

    override suspend fun upsertPlaylistItems(
        audioId: String,
        playlistIds: List<Int>
    ): Either<Throwable, List<Long>> {
        return localPlaylistDataSource.upsertPlaylistItemsDbModel(audioId, playlistIds)
            .let { Either.Right(it) }
    }

    override suspend fun updatePlaylistItems(
        playlistId: Int,
        playlistItems: List<PlaylistAudioItem>
    ): Either<Throwable, Unit> {
        val playlistItemsDbModel = playlistItems.map { it.toPlaylistItemDbModel(playlistId) }
        return localPlaylistDataSource.updatePlaylistItems(playlistItemsDbModel)
            .let { Either.Right(Unit) }
    }

    override suspend fun resetPlaylistById(idPlaylist: Int): Either<Throwable, Unit> {
        return localPlaylistDataSource.resetPlaylistById(idPlaylist).let { Either.Right(Unit) }
    }

    override suspend fun deletePlaylistItemById(
        audioItemId: String,
        playlistId: Int
    ): Either<Throwable, Unit> {
        return localPlaylistDataSource.deletePlaylistItemDbModelById(audioItemId, playlistId)
            .let { Either.Right(Unit) }
    }

    override suspend fun deletePlaylistById(playlistId: Int): Either<Throwable, Unit> {
        return localPlaylistDataSource.deletePlaylistDbModelById(playlistId)
            .let { Either.Right(Unit) }
    }

    override suspend fun deleteAllPlaylists(): Either<Throwable, Unit> {
        return localPlaylistDataSource.deleteAllPlaylists().let { Either.Right(Unit) }
    }
}
