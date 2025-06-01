package soy.gabimoreno.data.remote.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.local.playlist.LocalPlaylistDataSource
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.DefaultPlaylistRepository
import soy.gabimoreno.fake.buildPlaylist

class DefaultPlaylistRepositoryTest {

    private val localPlaylistDataSource: LocalPlaylistDataSource = relaxedMockk()
    private lateinit var repository: DefaultPlaylistRepository

    @Before
    fun setUp() {
        repository = DefaultPlaylistRepository(localPlaylistDataSource)
    }

    @Test
    fun `GIVEN valid parameters WHEN insertPlaylist THEN returns Right with playlist`() = runTest {
        val name = "Playlist Name"
        val description = "Playlist Description"
        val playList = Playlist(
            id = 1,
            title = name,
            description = description,
            items = emptyList(),
            position = 0
        )
        coEvery { localPlaylistDataSource.insertPlaylist(name, description) } returns playList

        val result = repository.insertPlaylist(name, description)

        result shouldBeInstanceOf Either.Right::class
        result.getOrNull() shouldBeEqualTo playList
        coVerifyOnce {
            localPlaylistDataSource.insertPlaylist(name, description)
        }
    }

    @Test
    fun `GIVEN datasource returns true WHEN isEmpty THEN returns Right with true`() = runTest {
        coEvery { localPlaylistDataSource.isEmpty() } returns true

        val result = repository.isEmpty()

        result shouldBeInstanceOf Either.Right::class
        result.getOrNull() shouldBeEqualTo true
    }

    @Test
    fun `GIVEN datasource returns false WHEN isEmpty THEN returns Right with false`() = runTest {
        coEvery { localPlaylistDataSource.isEmpty() } returns false

        val result = repository.isEmpty()

        result shouldBeInstanceOf Either.Right::class
        result.getOrNull() shouldBeEqualTo false
    }

    @Test
    fun `GIVEN valid playlist WHEN savePlaylist THEN returns Right with Unit`() = runTest {
        val playlist = buildPlaylist()
        coJustRun { localPlaylistDataSource.savePlaylist(listOf(playlist)) }

        val result = repository.savePlaylist(playlist)

        result shouldBeInstanceOf Either.Right::class
        result.getOrNull() shouldBeEqualTo Unit
        coVerifyOnce {
            localPlaylistDataSource.savePlaylist(listOf(playlist))
        }
    }

    @Test
    fun `GIVEN datasource returns playlists WHEN getAllPlaylists THEN returns Right with playlists`() =
        runTest {
            val playlists = listOf(buildPlaylist())
            coEvery { localPlaylistDataSource.getAllPlaylists() } returns playlists

            val result = repository.getAllPlaylists()

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo playlists
        }

    @Test
    fun `GIVEN empty playlists WHEN getAllPlaylists THEN returns Right with empty list`() =
        runTest {
            coEvery { localPlaylistDataSource.getAllPlaylists() } returns emptyList()

            val result = repository.getAllPlaylists()

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo emptyList()
        }

    @Test
    fun `GIVEN valid playlist id WHEN getPlaylistById THEN returns Right with flow`() = runTest {
        val playlistId = 1
        val playlist = buildPlaylist()
        val flow = flowOf(playlist)
        every { localPlaylistDataSource.getPlaylistById(playlistId) } returns flow

        val result = repository.getPlaylistById(playlistId)

        result shouldBeInstanceOf Either.Right::class
        result.getOrNull() shouldBeEqualTo flow
    }

    @Test
    fun `GIVEN non-existent playlist id WHEN getPlaylistById THEN returns Right with null flow`() =
        runTest {
            val playlistId = -1
            val flow = flowOf(null)
            every { localPlaylistDataSource.getPlaylistById(playlistId) } returns flow

            val result = repository.getPlaylistById(playlistId)

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo flow
        }

    @Test
    fun `GIVEN valid playlist id WHEN resetPlaylistById THEN returns Right with Unit`() = runTest {
        val playlistId = 1
        coJustRun {
            localPlaylistDataSource.resetPlaylistById(playlistId)
        }

        val result = repository.resetPlaylistById(playlistId)

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

            val result = repository.deleteAllPlaylists()

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo Unit
            coVerifyOnce {
                localPlaylistDataSource.deleteAllPlaylists()
            }
        }
}
