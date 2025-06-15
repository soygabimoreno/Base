package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPlaylist

class UpsertPlaylistsUseCaseTest {

    private val repository = mockk<PlaylistRepository>()
    private lateinit var useCase: UpsertPlaylistsUseCase

    @Before
    fun setUp() {
        useCase = UpsertPlaylistsUseCase(repository)
    }

    @Test
    fun `GIVEN valid list of playlist WHEN invoke THEN Right unit is returned`() = runTest {
        val playlists = listOf(buildPlaylist(), buildPlaylist(2))
        coEvery { repository.upsertPlaylists(playlists) } returns right(Unit)

        val result = useCase(playlists)

        result shouldBeEqualTo right(Unit)
        coVerifyOnce {
            repository.upsertPlaylists(playlists)
        }
    }

    @Test
    fun `GIVEN repository fails WHEN invoke THEN Left with throwable is returned`() = runTest {
        val playlists = listOf(buildPlaylist(), buildPlaylist(2))
        val exception = IllegalStateException("unexpected failure")
        coEvery { repository.upsertPlaylists(playlists) } throws exception

        val result = runCatching {
            useCase(playlists)
        }.getOrDefault(left(exception))

        result shouldBeEqualTo left(exception)
        coVerifyOnce {
            repository.upsertPlaylists(playlists)
        }
    }
}
