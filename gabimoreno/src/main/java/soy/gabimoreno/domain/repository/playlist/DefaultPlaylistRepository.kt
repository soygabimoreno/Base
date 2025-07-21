package soy.gabimoreno.domain.repository.playlist

import arrow.core.Either
import kotlinx.coroutines.flow.first
import soy.gabimoreno.data.cloud.audiosync.mappers.toCloudPlaylistItemResponse
import soy.gabimoreno.data.cloud.audiosync.mappers.toCloudPlaylistResponse
import soy.gabimoreno.data.cloud.audiosync.mappers.toPlaylist
import soy.gabimoreno.data.cloud.audiosync.mappers.toPlaylistItemDbModel
import soy.gabimoreno.data.cloud.playlist.datasource.CloudPlaylistDataSource
import soy.gabimoreno.data.cloud.playlist.response.CloudPlaylistItemResponse
import soy.gabimoreno.data.local.mapper.toPlaylistItemDbModel
import soy.gabimoreno.data.local.playlist.LocalPlaylistDataSource
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPlaylistRepository
    @Inject
    constructor(
        private val cloudPlaylistDataSource: CloudPlaylistDataSource,
        private val localPlaylistDataSource: LocalPlaylistDataSource,
    ) : PlaylistRepository {
        override suspend fun isEmpty(): Either<Throwable, Boolean> =
            localPlaylistDataSource.isEmpty().let {
                Either.Right(it)
            }

        override suspend fun insertPlaylist(
            name: String,
            description: String,
            email: String,
        ): Either<Throwable, Playlist> {
            val playlist = localPlaylistDataSource.insertPlaylist(name, description)
            if (email.isNotEmpty()) {
                cloudPlaylistDataSource.updatePlaylist(
                    email = email,
                    playlist = playlist.toCloudPlaylistResponse(),
                )
            }
            return Either.Right(playlist)
        }

        override suspend fun savePlaylist(
            playlist: Playlist,
            email: String,
        ): Either<Throwable, Unit> {
            if (email.isNotEmpty()) {
                cloudPlaylistDataSource.updatePlaylist(
                    email = email,
                    playlist = playlist.toCloudPlaylistResponse(),
                )
            }
            return localPlaylistDataSource
                .savePlaylist(listOf(playlist))
                .let { Either.Right(Unit) }
        }

        override suspend fun getAllPlaylists(email: String): Either<Throwable, List<Playlist>> {
            if (email.isNotEmpty()) {
                val playlists = cloudPlaylistDataSource.getPlaylists(email)
                localPlaylistDataSource.upsertPlaylistDbModels(playlists.map { it.toPlaylist() })
                playlists.forEach { playlist ->
                    val audioItems =
                        cloudPlaylistDataSource.getPlaylistItems(email, playlist.playlistId)
                    localPlaylistDataSource.upsertPlaylistItemsDbModel(
                        audioItems.map {
                            it.toPlaylistItemDbModel()
                        },
                    )
                }
            }
            return localPlaylistDataSource.getAllPlaylists().let { Either.Right(it) }
        }

        override suspend fun getPlaylistById(idPlaylist: Int): Either<Throwable, Playlist?> =
            localPlaylistDataSource.getPlaylistById(idPlaylist).let {
                Either.Right(it.first())
            }

        override suspend fun getPlaylistIdsByItemId(
            audioItemId: String,
        ): Either<Throwable, List<Int>> =
            localPlaylistDataSource
                .getPlaylistIdsByItemId(audioItemId)
                .let { Either.Right(it) }

        override suspend fun upsertPlaylists(
            playlists: List<Playlist>,
            email: String,
        ): Either<Throwable, Unit> {
            if (email.isNotEmpty()) {
                playlists.forEach { playlist ->
                    cloudPlaylistDataSource.updatePlaylist(
                        email = email,
                        playlist = playlist.toCloudPlaylistResponse(),
                    )
                }
            }
            return localPlaylistDataSource
                .upsertPlaylistDbModels(playlists)
                .let { Either.Right(Unit) }
        }

        override suspend fun upsertPlaylistItems(
            audioId: String,
            playlistIds: List<Int>,
            email: String,
        ): Either<Throwable, List<Long>> {
            val result = localPlaylistDataSource.upsertPlaylistItemsDbModel(audioId, playlistIds)

            if (email.isNotEmpty()) {
                val startPosition =
                    localPlaylistDataSource.getTotalPlaylistItems() - playlistIds.size + 1

                playlistIds.forEachIndexed { index, playlistId ->
                    val position = startPosition + index
                    cloudPlaylistDataSource.savePlaylistItem(
                        email = email,
                        playlistItem =
                            CloudPlaylistItemResponse(
                                id = position.toString(),
                                audioItemId = audioId,
                                playlistId = playlistId.toString(),
                                position = position,
                            ),
                    )
                }
            }

            return Either.Right(result)
        }

        override suspend fun updatePlaylistItems(
            playlistId: Int,
            playlistItems: List<PlaylistAudioItem>,
            email: String,
        ): Either<Throwable, Unit> {
            if (email.isNotEmpty()) {
                playlistItems.forEachIndexed { index, playlistAudioItem ->
                    cloudPlaylistDataSource.savePlaylistItem(
                        email = email,
                        playlistItem = playlistAudioItem.toCloudPlaylistItemResponse(playlistId),
                    )
                }
            }
            val playlistItemsDbModel = playlistItems.map { it.toPlaylistItemDbModel(playlistId) }
            return localPlaylistDataSource
                .updatePlaylistItems(playlistItemsDbModel)
                .let { Either.Right(Unit) }
        }

        override suspend fun resetPlaylistById(
            idPlaylist: Int,
            email: String,
        ): Either<Throwable, Unit> =
            localPlaylistDataSource.resetPlaylistById(idPlaylist).let {
                Either.Right(Unit)
            }

        override suspend fun deletePlaylistItemById(
            audioItemId: String,
            playlistId: Int,
            email: String,
        ): Either<Throwable, Unit> {
            if (email.isNotEmpty()) {
                val itemToDelete =
                    localPlaylistDataSource.getPlaylistItemsCountByPlaylistId(
                        audioItemId,
                        playlistId,
                    )
                cloudPlaylistDataSource.deletePlaylistItem(
                    email = email,
                    playlistId = playlistId.toString(),
                    itemId = itemToDelete.toString(),
                )
            }
            return localPlaylistDataSource
                .deletePlaylistItemDbModelById(audioItemId, playlistId)
                .let { Either.Right(Unit) }
        }

        override suspend fun deletePlaylistById(
            playlistId: Int,
            email: String,
        ): Either<Throwable, Unit> {
            if (email.isNotEmpty()) {
                cloudPlaylistDataSource.deletePlaylist(
                    email = email,
                    playlistId = playlistId.toString(),
                )
            }
            return localPlaylistDataSource
                .deletePlaylistDbModelById(playlistId)
                .let { Either.Right(Unit) }
        }

        override suspend fun deleteAllPlaylists(email: String): Either<Throwable, Unit> {
            if (email.isNotEmpty()) {
                cloudPlaylistDataSource.deleteAllPlaylists(email)
            }
            return localPlaylistDataSource.deleteAllPlaylists().let { Either.Right(Unit) }
        }
    }
