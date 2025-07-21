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
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPlaylistItems
import soy.gabimoreno.framework.datastore.getEmail

class UpdatePlaylistItemsUseCaseTest {
    private val context: Context = mockk()
    private val repository = mockk<PlaylistRepository>()

    private lateinit var useCase: UpdatePlaylistItemsUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = UpdatePlaylistItemsUseCase(context, repository)
    }

    @Test
    fun `GIVEN valid input WHEN invoke THEN repository is called and success is returned`() =
        runTest {
            val playlistId = 1
            val playlistAudioItems = buildPlaylistItems()
            coEvery {
                repository.updatePlaylistItems(playlistId, playlistAudioItems, EMAIL)
            } returns right(Unit)

            val result = useCase(playlistId, playlistAudioItems)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                repository.updatePlaylistItems(playlistId, playlistAudioItems, EMAIL)
            }
        }

    @Test
    fun `GIVEN repository fails WHEN invoke THEN error is returned`() =
        runTest {
            val playlistId = 1
            val playlistAudioItems = buildPlaylistItems()
            val throwable = RuntimeException()
            coEvery {
                repository.updatePlaylistItems(playlistId, playlistAudioItems, EMAIL)
            } returns left(throwable)

            val result = useCase(playlistId, playlistAudioItems)

            result shouldBeEqualTo left(throwable)
            coVerifyOnce {
                repository.updatePlaylistItems(playlistId, playlistAudioItems, EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
