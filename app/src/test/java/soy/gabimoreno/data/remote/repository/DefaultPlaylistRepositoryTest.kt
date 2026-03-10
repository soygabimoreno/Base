package soy.gabimoreno.data.remote.repository

import arrow.core.Either
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.cloud.audiosync.mappers.toCloudPlaylistResponse
import soy.gabimoreno.data.cloud.playlist.datasource.CloudPlaylistDataSource
import soy.gabimoreno.data.local.mapper.toPlaylistItemDbModel
import soy.gabimoreno.data.local.playlist.LocalPlaylistDataSource
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.DefaultPlaylistRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPlaylist
import soy.gabimoreno.fake.buildPlaylistItems

class DefaultPlaylistRepositoryTest {
    private val cloudDataSource: CloudPlaylistDataSource = mockk()
    private val localPlaylistDataSource: LocalPlaylistDataSource = relaxedMockk()
    private lateinit var repository: DefaultPlaylistRepository

    @Before
    fun setUp() {
        repository =
            DefaultPlaylistRepository(
                cloudDataSource,
                localPlaylistDataSource,
            )
    }

    @Test
    fun `GIVEN valid parameters WHEN insertPlaylist THEN returns Right with playlist`() =
        runTest {
            val name = "Playlist Name"
            val description = "Playlist Description"
            val playList =
                Playlist(
                    id = 1,
                    title = name,
                    description = description,
                    items = emptyList(),
                    position = 0,
                )
            coEvery { localPlaylistDataSource.insertPlaylist(name, description) } returns playList
            coJustRun { cloudDataSource.updatePlaylist(EMAIL, playList.toCloudPlaylistResponse()) }

            val result = repository.insertPlaylist(name, description, EMAIL)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo playList
            coVerifyOnce {
                localPlaylistDataSource.insertPlaylist(name, description)
                cloudDataSource.updatePlaylist(EMAIL, playList.toCloudPlaylistResponse())
            }
        }

    @Test
    fun `GIVEN valid parameters and empty email WHEN insertPlaylist THEN returns Right and does not update cloud`() =
        runTest {
            val name = "Playlist Name"
            val description = "Playlist Description"
            val playList =
                Playlist(
                    id = 1,
                    title = name,
                    description = description,
                    items = emptyList(),
                    position = 0,
                )
            coEvery { localPlaylistDataSource.insertPlaylist(name, description) } returns playList

            val result = repository.insertPlaylist(name, description, EMPTY_STRING)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo playList
            coVerifyOnce {
                localPlaylistDataSource.insertPlaylist(name, description)
            }
            coVerifyNever {
                cloudDataSource.updatePlaylist(EMAIL, playList.toCloudPlaylistResponse())
            }
        }

    @Test
    fun `GIVEN datasource returns true WHEN isEmpty THEN returns Right with true`() =
        runTest {
            coEvery { localPlaylistDataSource.isEmpty() } returns true

            val result = repository.isEmpty()

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo true
        }

    @Test
    fun `GIVEN datasource returns false WHEN isEmpty THEN returns Right with false`() =
        runTest {
            coEvery { localPlaylistDataSource.isEmpty() } returns false

            val result = repository.isEmpty()

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo false
        }

    @Test
    fun `GIVEN valid playlist WHEN savePlaylist THEN returns Right with Unit`() =
        runTest {
            val playlist = buildPlaylist()
            coJustRun { localPlaylistDataSource.savePlaylist(listOf(playlist)) }
            coJustRun { cloudDataSource.updatePlaylist(EMAIL, playlist.toCloudPlaylistResponse()) }

            val result = repository.savePlaylist(playlist, EMAIL)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo Unit
            coVerifyOnce {
                localPlaylistDataSource.savePlaylist(listOf(playlist))
                cloudDataSource.updatePlaylist(EMAIL, playlist.toCloudPlaylistResponse())
            }
        }

    @Test
    fun `GIVEN playlist and empty email WHEN savePlaylist THEN updates local only`() =
        runTest {
            val playlist = buildPlaylist()
            coJustRun { localPlaylistDataSource.savePlaylist(listOf(playlist)) }

            val result = repository.savePlaylist(playlist, EMPTY_STRING)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                localPlaylistDataSource.savePlaylist(listOf(playlist))
            }
            coVerifyNever {
                cloudDataSource.updatePlaylist(any(), any())
            }
        }

    @Test
    fun `GIVEN empty email WHEN getAllPlaylists THEN returns Right with local playlists only`() =
        runTest {
            val playlists = listOf(buildPlaylist())
            coEvery { localPlaylistDataSource.getAllPlaylists() } returns playlists

            val result = repository.getAllPlaylists(EMPTY_STRING)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo playlists
        }

    @Test
    fun `GIVEN empty playlists and email is empty WHEN getAllPlaylists THEN returns Right with empty list`() =
        runTest {
            coEvery { localPlaylistDataSource.getAllPlaylists() } returns emptyList()

            val result = repository.getAllPlaylists(EMPTY_STRING)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo emptyList()
        }

    @Test
    fun `GIVEN valid playlist id WHEN getPlaylistById THEN returns Right with flow`() =
        runTest {
            val playlistId = 1
            val playlist = buildPlaylist()
            every { localPlaylistDataSource.getPlaylistById(playlistId) } returns flowOf(playlist)

            val result = repository.getPlaylistById(playlistId)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo playlist
        }

    @Test
    fun `GIVEN non-existent playlist id WHEN getPlaylistById THEN returns Right with null`() =
        runTest {
            val playlistId = -1
            every { localPlaylistDataSource.getPlaylistById(playlistId) } returns flowOf(null)

            val result = repository.getPlaylistById(playlistId)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo null
        }

    @Test
    fun `GIVEN valid playlist id WHEN resetPlaylistById THEN returns Right with Unit`() =
        runTest {
            val playlistId = 1
            coJustRun {
                localPlaylistDataSource.resetPlaylistById(playlistId)
            }

            val result = repository.resetPlaylistById(playlistId, EMAIL)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo Unit
            coVerifyOnce {
                localPlaylistDataSource.resetPlaylistById(playlistId)
            }
        }

    @Test
    fun `GIVEN datasource succeeds WHEN deleteAllPlaylists THEN returns Right with Unit`() =
        runTest {
            coJustRun { localPlaylistDataSource.deleteAllPlaylists() }
            coJustRun { cloudDataSource.deleteAllPlaylists(EMAIL) }

            val result = repository.deleteAllPlaylists(EMAIL)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo Unit
            coVerifyOnce {
                localPlaylistDataSource.deleteAllPlaylists()
                cloudDataSource.deleteAllPlaylists(EMAIL)
            }
        }

    @Test
    fun `GIVEN empty email WHEN deleteAllPlaylists THEN deletes local only`() =
        runTest {
            coJustRun { localPlaylistDataSource.deleteAllPlaylists() }

            val result = repository.deleteAllPlaylists(EMPTY_STRING)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                localPlaylistDataSource.deleteAllPlaylists()
            }
            coVerifyNever {
                cloudDataSource.deleteAllPlaylists(any())
            }
        }

    @Test
    fun `GIVEN a valid playlistId WHEN getPlaylistIdsByItemId THEN playlistIds are returned`() =
        runTest {
            val playlist1 = buildPlaylist()
            val playlist2 = buildPlaylist(2)
            val playlistIds = listOf(playlist1.id, playlist2.id)
            coEvery {
                localPlaylistDataSource.getPlaylistIdsByItemId(playlist1.items.first().id)
            } returns playlistIds

            val result = repository.getPlaylistIdsByItemId(playlist1.items.first().id)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo playlistIds
            coVerifyOnce {
                localPlaylistDataSource.getPlaylistIdsByItemId(playlist1.items.first().id)
            }
        }

    @Test
    fun `GIVEN valid data WHEN upsertPlaylists THEN upserts playlists`() =
        runTest {
            val playlists = listOf(buildPlaylist(), buildPlaylist(2))
            coJustRun { localPlaylistDataSource.upsertPlaylistDbModels(playlists) }
            coJustRun {
                cloudDataSource.updatePlaylist(
                    EMAIL,
                    playlists[0].toCloudPlaylistResponse(),
                )
            }
            coJustRun {
                cloudDataSource.updatePlaylist(
                    EMAIL,
                    playlists[1].toCloudPlaylistResponse(),
                )
            }

            val result = repository.upsertPlaylists(playlists, EMAIL)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                localPlaylistDataSource.upsertPlaylistDbModels(playlists)
                cloudDataSource.updatePlaylist(EMAIL, playlists[0].toCloudPlaylistResponse())
                cloudDataSource.updatePlaylist(EMAIL, playlists[1].toCloudPlaylistResponse())
            }
        }

    @Test
    fun `GIVEN data source throws exception WHEN upsertPlaylists THEN Left with throwable is returned`() =
        runTest {
            val playlists = listOf(buildPlaylist(), buildPlaylist(2))
            val exception = RuntimeException("Something went wrong")
            coEvery {
                localPlaylistDataSource.upsertPlaylistDbModels(playlists)
            } throws exception
            coJustRun { cloudDataSource.updatePlaylist(EMPTY_STRING, any()) }

            val result =
                runCatching {
                    repository.upsertPlaylists(playlists, EMPTY_STRING)
                }.getOrDefault(left(exception))

            result shouldBeEqualTo left(exception)
            coVerifyOnce {
                localPlaylistDataSource.upsertPlaylistDbModels(playlists)
            }
            coVerifyNever {
                cloudDataSource.updatePlaylist(EMAIL, any())
            }
        }

    @Test
    fun `GIVEN playlists and empty email WHEN upsertPlaylists THEN updates local only`() =
        runTest {
            val playlists = listOf(buildPlaylist(), buildPlaylist(2))
            coJustRun { localPlaylistDataSource.upsertPlaylistDbModels(playlists) }

            val result = repository.upsertPlaylists(playlists, EMPTY_STRING)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                localPlaylistDataSource.upsertPlaylistDbModels(playlists)
            }
            coVerifyNever {
                cloudDataSource.updatePlaylist(any(), any())
            }
        }

    @Test
    fun `GIVEN playlistIds WHEN upsertPlaylistItemsDbModel THEN upserts items with correct positions`() =
        runTest {
            val playlistItemId = "audio-123"
            val playlistIds = listOf(1, 2)
            val expectedIds = listOf(101L, 102L)
            val totalItems = 10
            coEvery {
                localPlaylistDataSource.upsertPlaylistItemsDbModel(playlistItemId, playlistIds)
            } returns expectedIds
            coEvery { localPlaylistDataSource.getTotalPlaylistItems() } returns totalItems
            coJustRun { cloudDataSource.savePlaylistItem(EMAIL, any()) }

            val result = repository.upsertPlaylistItems(playlistItemId, playlistIds, EMAIL)

            result shouldBeEqualTo right(expectedIds)
            coVerifyOnce {
                localPlaylistDataSource.upsertPlaylistItemsDbModel(playlistItemId, playlistIds)
            }
            coVerify(exactly = 2) {
                cloudDataSource.savePlaylistItem(EMAIL, any())
            }
        }

    @Test
    fun `GIVEN data source throws exception WHEN upsertPlaylistItemsDbModel THEN Left with throwable is returned`() =
        runTest {
            val playlistItemId = "audio-123"
            val playlistIds = listOf(1, 2)
            val exception = RuntimeException("Something went wrong")

            coEvery {
                localPlaylistDataSource.upsertPlaylistItemsDbModel(playlistItemId, playlistIds)
            } throws exception

            val result =
                runCatching {
                    repository.upsertPlaylistItems(playlistItemId, playlistIds, EMAIL)
                }.getOrDefault(left(exception))

            result shouldBeEqualTo left(exception)
            coVerifyOnce {
                localPlaylistDataSource.upsertPlaylistItemsDbModel(playlistItemId, playlistIds)
            }
        }

    @Test
    fun `GIVEN valid playlistItemId WHEN deletePlaylistItemByPlaylistId THEN deletes playlist item`() =
        runTest {
            val audioItemId = "audio-123"
            val playlistId = 1
            val itemToDelete = 1
            coJustRun {
                localPlaylistDataSource.deletePlaylistItemDbModelById(
                    audioItemId,
                    playlistId,
                )
            }
            coEvery {
                localPlaylistDataSource.getPlaylistItemsCountByPlaylistId(audioItemId, playlistId)
            } returns itemToDelete
            coJustRun {
                cloudDataSource.deletePlaylistItem(
                    EMAIL,
                    playlistId.toString(),
                    itemToDelete.toString(),
                )
            }

            val result = repository.deletePlaylistItemById(audioItemId, playlistId, EMAIL)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                localPlaylistDataSource.deletePlaylistItemDbModelById(audioItemId, playlistId)
                localPlaylistDataSource.getPlaylistItemsCountByPlaylistId(audioItemId, playlistId)
                cloudDataSource.deletePlaylistItem(
                    EMAIL,
                    playlistId.toString(),
                    itemToDelete.toString(),
                )
            }
        }

    @Test
    fun `GIVEN playlist items WHEN updatePlaylistItems THEN data source is called and Right is returned`() =
        runTest {
            val playlistId = 42
            val playlistAudioItems = buildPlaylistItems()
            val playlistItemsDbModel =
                playlistAudioItems.map { playlistAudioItem ->
                    playlistAudioItem.toPlaylistItemDbModel(playlistId)
                }
            coJustRun { cloudDataSource.savePlaylistItem(EMAIL, any()) }

            val result = repository.updatePlaylistItems(playlistId, playlistAudioItems, EMAIL)

            result shouldBeEqualTo Either.Right(Unit)
            coVerifyOnce {
                localPlaylistDataSource.updatePlaylistItems(playlistItemsDbModel)
            }
            coVerify(exactly = 3) {
                cloudDataSource.savePlaylistItem(EMAIL, any())
            }
        }

    @Test
    fun `GIVEN playlistId WHEN deletePlaylistById THEN deletes playlist`() =
        runTest {
            val playlistId = 1
            coJustRun { localPlaylistDataSource.deletePlaylistDbModelById(playlistId) }
            coJustRun { cloudDataSource.deletePlaylist(EMAIL, playlistId.toString()) }

            val result = repository.deletePlaylistById(playlistId, EMAIL)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                localPlaylistDataSource.deletePlaylistDbModelById(playlistId)
                cloudDataSource.deletePlaylist(EMAIL, playlistId.toString())
            }
        }

    @Test
    fun `GIVEN playlistId and empty email WHEN deletePlaylistById THEN deletes local only`() =
        runTest {
            val playlistId = 1
            coJustRun { localPlaylistDataSource.deletePlaylistDbModelById(playlistId) }

            val result = repository.deletePlaylistById(playlistId, EMPTY_STRING)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                localPlaylistDataSource.deletePlaylistDbModelById(playlistId)
            }
            coVerifyNever {
                cloudDataSource.deletePlaylist(any(), any())
            }
        }
}

private const val EMAIL = "test@test.com"
private const val EMPTY_STRING = ""
