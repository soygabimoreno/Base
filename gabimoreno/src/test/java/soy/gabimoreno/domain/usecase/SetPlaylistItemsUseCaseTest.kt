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

class SetPlaylistItemsUseCaseTest {

    private val repository: PlaylistRepository = relaxedMockk()
    private lateinit var useCase: SetPlaylistItemsUseCase

    @Before
    fun setUp() {
        useCase = SetPlaylistItemsUseCase(repository)
    }

    @Test
    fun `GIVEN valid playlistIds WHEN invoke THEN Right with inserted ids is returned`() = runTest {
        val playlistItemId = "audio-123"
        val playlistIds = listOf(1, 2)
        val expectedIds = listOf(201L, 202L)

        coEvery {
            repository.upsertPlaylistItems(playlistItemId, playlistIds)
        } returns right(expectedIds)

        val result = useCase(playlistItemId, playlistIds)

        result shouldBeEqualTo right(expectedIds)
        coVerifyOnce {
            repository.upsertPlaylistItems(playlistItemId, playlistIds)
        }
    }

    @Test
    fun `GIVEN repository fails WHEN invoke THEN Left with throwable is returned`() = runTest {
        val playlistItemId = "audio-123"
        val playlistIds = listOf(1, 2)
        val exception = IllegalStateException("unexpected failure")

        coEvery {
            repository.upsertPlaylistItems(playlistItemId, playlistIds)
        } returns left(exception)

        val result = useCase(playlistItemId, playlistIds)

        result shouldBeEqualTo left(exception)
        coVerifyOnce {
            repository.upsertPlaylistItems(playlistItemId, playlistIds)
        }
    }
}
