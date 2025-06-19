package soy.gabimoreno.domain.usecase

import io.mockk.mockk
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository

class DeletePlaylistByIdUseCaseTest {

    private val playlistRepository: PlaylistRepository = mockk()
    private lateinit var useCase: DeletePlaylistByIdUseCase

    @Before
    fun setUp() {
        useCase = DeletePlaylistByIdUseCase(playlistRepository)
    }

    @Test
    fun `GIVEN valid playlistId WHEN invoke THEN returns Right with Unit`() = runTest {
        val playlistId = 1
        coEvery {
            playlistRepository.deletePlaylistById(playlistId)
        } returns right(Unit)

        val result = useCase(playlistId)

        result shouldBeEqualTo right(Unit)
        coVerifyOnce {
            playlistRepository.deletePlaylistById(playlistId)
        }
    }

    @Test
    fun `GIVEN repository returns Left WHEN invoke THEN returns Left with exception`() = runTest {
        val playlistId = 1
        val exception = RuntimeException("Database error")
        coEvery {
            playlistRepository.deletePlaylistById(playlistId)
        } returns left(exception)

        val result = useCase(playlistId)

        result shouldBeEqualTo left(exception)
    }
}
