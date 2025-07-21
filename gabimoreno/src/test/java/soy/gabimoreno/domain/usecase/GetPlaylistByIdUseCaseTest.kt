package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPlaylist

class GetPlaylistByIdUseCaseTest {
    private val playlistRepository: PlaylistRepository = relaxedMockk()
    private lateinit var useCase: GetPlaylistByIdUseCase

    @Before
    fun setUp() {
        useCase = GetPlaylistByIdUseCase(playlistRepository)
    }

    @Test
    fun `GIVEN valid playlist id WHEN invoke THEN returns right with playlist`() =
        runTest {
            val playlist = buildPlaylist()
            coEvery { playlistRepository.getPlaylistById(playlist.id) } returns right(playlist)

            val result = useCase(playlist.id)

            result shouldBeEqualTo right(playlist)
            coVerifyOnce {
                playlistRepository.getPlaylistById(playlist.id)
            }
        }

    @Test
    fun `GIVEN repository throws exception WHEN invoke THEN returns left with throwable`() =
        runTest {
            val playlistId = 123
            val exception = RuntimeException("Database error")
            coEvery { playlistRepository.getPlaylistById(playlistId) } returns left(exception)

            val result = useCase(playlistId)

            result shouldBeEqualTo left(exception)
            coVerifyOnce {
                playlistRepository.getPlaylistById(playlistId)
            }
        }

    @Test
    fun `GIVEN empty playlistId WHEN invoke THEN delegates to repository`() =
        runTest {
            val playlist = buildPlaylist()
            coEvery { playlistRepository.getPlaylistById(playlist.id) } returns right(playlist)

            val result = useCase(playlist.id)

            result shouldBeEqualTo right(playlist)
            coVerifyOnce {
                playlistRepository.getPlaylistById(playlist.id)
            }
        }
}
