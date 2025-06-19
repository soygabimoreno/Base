@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.data.local.playlist

import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.GabiMorenoDatabase
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseDbModel
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseItemDbModel
import soy.gabimoreno.data.local.mapper.toPlaylistAudioItem
import soy.gabimoreno.data.local.mapper.toPlaylistDbModel
import soy.gabimoreno.data.local.mapper.toPlaylistMapper
import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistWithItems
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.content.Playlist
import javax.inject.Inject

class LocalPlaylistDataSource @Inject constructor(
    gabiMorenoDatabase: GabiMorenoDatabase,
    @IO private val dispatcher: CoroutineDispatcher,
) {

    @VisibleForTesting
    val playlistDbModelDao = gabiMorenoDatabase.playlistDbModelDao()

    @VisibleForTesting
    val playlistItemDbModelDao = gabiMorenoDatabase.playlistItemDbModelDao()

    @VisibleForTesting
    val playlistTransactionDao = gabiMorenoDatabase.playlistTransactionDao()

    @VisibleForTesting
    val audioCourseDbModelDao = gabiMorenoDatabase.audioCourseDbModelDao()

    @VisibleForTesting
    val audioCourseItemDbModelDao = gabiMorenoDatabase.audioCourseItemDbModelDao()

    @VisibleForTesting
    val premiumAudioDbModelDao = gabiMorenoDatabase.premiumAudioDbModelDao()

    suspend fun isEmpty(): Boolean = withContext(dispatcher) {
        playlistDbModelDao.getTotalPlaylists() <= 0
    }

    suspend fun insertPlaylist(name: String, description: String): Playlist =
        withContext(dispatcher) {
            val lastPosition = playlistDbModelDao.getTotalPlaylists()
            val playlistId = playlistDbModelDao.insertPlaylistDbModel(
                playlistDbModel = PlaylistDbModel(
                    title = name,
                    description = description,
                    position = lastPosition
                )
            )
            Playlist(
                id = playlistId.toInt(),
                title = name,
                description = description,
                items = emptyList(),
                position = lastPosition
            )
        }

    suspend fun savePlaylist(playlists: List<Playlist>) = withContext(dispatcher) {
        playlistTransactionDao.upsertPlaylistsWithItems(playlists)
    }

    suspend fun getAllPlaylists(): List<Playlist> = withContext(dispatcher) {
        val playlistsWithItems = playlistTransactionDao.getPlaylistsWithItems()
        val allItemIds = playlistsWithItems.flatMap { it.items }.map { it.audioItemId }.toSet()
        val resources = loadAudioResources(allItemIds)

        playlistsWithItems.map { it.mapToPlaylist(resources) }
    }

    fun getPlaylistById(idPlaylist: Int): Flow<Playlist?> {
        return playlistTransactionDao.getPlaylistWithItemsById(idPlaylist)
            .mapLatest { playlistWithItems ->
                if (playlistWithItems == null) return@mapLatest null

                val itemIds = playlistWithItems.items
                    .map { playlistItemDbModel -> playlistItemDbModel.audioItemId }.toSet()
                val resources = loadAudioResources(itemIds)

                playlistWithItems.mapToPlaylist(resources)
            }
            .flowOn(dispatcher)
    }

    suspend fun resetPlaylistById(idPlaylist: Int) = withContext(dispatcher) {
        playlistItemDbModelDao.deletePlaylistItemsDbModelByPlaylistId(idPlaylist)
    }

    suspend fun deleteAllPlaylists() = withContext(dispatcher) {
        playlistDbModelDao.deleteAllPlaylistDbModels()
    }

    suspend fun getPlaylistIdsByItemId(audioItemId: String): List<Int> =
        withContext(dispatcher) {
            playlistTransactionDao.getPlaylistIdsByItemId(audioItemId)
        }

    suspend fun upsertPlaylistDbModels(
        playlists: List<Playlist>
    ) = withContext(dispatcher) {
        playlistDbModelDao.upsertPlaylistDbModels(
            playlists.map { it.toPlaylistDbModel() }
        )
    }

    suspend fun upsertPlaylistItemsDbModel(
        audioId: String,
        playlistIds: List<Int>
    ): List<Long> =
        withContext(dispatcher) {
            val lastPosition = playlistItemDbModelDao.getTotalPlaylistItems()
            val playlistItems = playlistIds.mapIndexed { index, playlistId ->
                PlaylistItemsDbModel(
                    audioItemId = audioId,
                    playlistId = playlistId,
                    position = index + lastPosition
                )
            }
            playlistItemDbModelDao.upsertPlaylistItemsDbModel(playlistItems)
        }

    suspend fun updatePlaylistItems(playlistItems: List<PlaylistItemsDbModel>) =
        withContext(dispatcher) {
            playlistItemDbModelDao.upsertPlaylistItemsDbModel(playlistItems)
        }

    suspend fun deletePlaylistDbModelById(playlistId: Int) = withContext(dispatcher) {
        playlistDbModelDao.deletePlaylistDbModelById(playlistId)
        playlistItemDbModelDao.deletePlaylistItemsDbModelByPlaylistId(playlistId)
    }

    suspend fun deletePlaylistItemDbModelById(
        audioItemId: String,
        playlistId: Int
    ) =
        withContext(dispatcher) {
            playlistItemDbModelDao.deletePlaylistItemDbModelById(audioItemId, playlistId)
        }

    private suspend fun loadAudioResources(ids: Set<String>): AudioResources {
        val premiumAudioMap = premiumAudioDbModelDao
            .getPremiumAudiosByIds(ids)
            .associateBy { it.id }

        val audioCoursesIds =
            ids.filter { it.contains("-") }
                .map { id -> id.substringBefore("-") }
                .toSet()
        val audioCoursesMap = audioCourseDbModelDao
            .getAudioCoursesByIds(audioCoursesIds)
            .associateBy { it.id }

        val audioCourseItemsMap = audioCourseItemDbModelDao
            .getAudioCourseItemsByIds(ids)
            .associateBy { it.id }

        return AudioResources(premiumAudioMap, audioCoursesMap, audioCourseItemsMap)
    }

    private fun PlaylistWithItems.mapToPlaylist(resources: AudioResources): Playlist {
        val playlistItems = items
            .sortedBy { it.position }
            .mapNotNull { item ->
                val position = item.position
                val playlistItemId = item.id
                val audioItem = if (item.audioItemId.contains("-")) {
                    val course = resources.audioCourses[item.audioItemId.substringBefore("-")]
                    val courseItem = resources.audioCourseItems[item.audioItemId]
                    if (course == null || courseItem == null) return@mapNotNull null
                    courseItem.toPlaylistAudioItem(course, position)
                } else {
                    resources.premiumAudios[item.audioItemId]?.toPlaylistAudioItem(position)
                }
                audioItem?.copy(playlistItemId = playlistItemId)
            }

        return playlist.toPlaylistMapper(playlistItems)
    }

    private data class AudioResources(
        val premiumAudios: Map<String, PremiumAudioDbModel>,
        val audioCourses: Map<String, AudioCourseDbModel>,
        val audioCourseItems: Map<String, AudioCourseItemDbModel>,
    )
}
