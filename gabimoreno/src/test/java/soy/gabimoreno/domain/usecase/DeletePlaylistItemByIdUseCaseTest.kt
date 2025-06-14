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

class DeletePlaylistItemByIdUseCaseTest {

    private val playlistRepository: PlaylistRepository = mockk()
    private lateinit var useCase: DeletePlaylistItemByIdUseCase

    @Before
    fun setUp() {
        useCase = DeletePlaylistItemByIdUseCase(playlistRepository)
    }

    @Test
    fun `GIVEN valid playlistItemId WHEN invoke THEN returns Right with Unit`() = runTest {
        val audioItemId = "audio-123"
        val playlistId = 1
        coEvery {
            playlistRepository.deletePlaylistItemById(
                audioItemId,
                playlistId
            )
        } returns right(Unit)

        val result = useCase(audioItemId, playlistId)

        result shouldBeEqualTo right(Unit)
        coVerifyOnce {
            playlistRepository.deletePlaylistItemById(audioItemId, playlistId)
        }
    }

    @Test
    fun `GIVEN repository returns Left WHEN invoke THEN returns Left with exception`() = runTest {
        val audioItemId = "audio-123"
        val playlistId = 1
        val exception = RuntimeException("Database error")
        coEvery {
            playlistRepository.deletePlaylistItemById(audioItemId, playlistId)
        } returns left(exception)

        val result = useCase(audioItemId, playlistId)

        result shouldBeEqualTo left(exception)
    }
}
