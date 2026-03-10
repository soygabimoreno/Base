package soy.gabimoreno.domain.usecase

import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.framework.datastore.getEmail

class SetPlaylistItemsUseCaseTest {
    private val context: Context = mockk()
    private val repository: PlaylistRepository = relaxedMockk()

    private lateinit var useCase: SetPlaylistItemsUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = SetPlaylistItemsUseCase(context, repository)
    }

    @Test
    fun `GIVEN valid playlistIds WHEN invoke THEN Right with inserted ids is returned`() =
        runTest {
            val playlistItemId = "audio-123"
            val playlistIds = listOf(1, 2)
            val expectedIds = listOf(201L, 202L)

            coEvery {
                repository.upsertPlaylistItems(playlistItemId, playlistIds, EMAIL)
            } returns right(expectedIds)

            val result = useCase(playlistItemId, playlistIds)

            result shouldBeEqualTo right(expectedIds)
            coVerifyOnce {
                repository.upsertPlaylistItems(playlistItemId, playlistIds, EMAIL)
            }
        }

    @Test
    fun `GIVEN repository fails WHEN invoke THEN Left with throwable is returned`() =
        runTest {
            val playlistItemId = "audio-123"
            val playlistIds = listOf(1, 2)
            val exception = IllegalStateException("unexpected failure")

            coEvery {
                repository.upsertPlaylistItems(playlistItemId, playlistIds, EMAIL)
            } returns left(exception)

            val result = useCase(playlistItemId, playlistIds)

            result shouldBeEqualTo left(exception)
            coVerifyOnce {
                repository.upsertPlaylistItems(playlistItemId, playlistIds, EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
