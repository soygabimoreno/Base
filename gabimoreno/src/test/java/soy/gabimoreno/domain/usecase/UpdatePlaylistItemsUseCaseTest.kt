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
import soy.gabimoreno.fake.buildPlaylistItems

class UpdatePlaylistItemsUseCaseTest {

    private val repository = mockk<PlaylistRepository>()
    private lateinit var useCase: UpdatePlaylistItemsUseCase

    @Before
    fun setUp() {
        useCase = UpdatePlaylistItemsUseCase(repository)
    }

    @Test
    fun `GIVEN valid input WHEN invoke THEN repository is called and success is returned`() =
        runTest {
            val playlistId = 1
            val playlistAudioItems = buildPlaylistItems()
            coEvery {
                repository.updatePlaylistItems(playlistId, playlistAudioItems)
            } returns right(Unit)

            val result = useCase(playlistId, playlistAudioItems)

            result shouldBeEqualTo right(Unit)
            coVerifyOnce {
                repository.updatePlaylistItems(playlistId, playlistAudioItems)
            }
        }

    @Test
    fun `GIVEN repository fails WHEN invoke THEN error is returned`() = runTest {
        val playlistId = 1
        val playlistAudioItems = buildPlaylistItems()
        val throwable = RuntimeException()
        coEvery {
            repository.updatePlaylistItems(playlistId, playlistAudioItems)
        } returns left(throwable)

        val result = useCase(playlistId, playlistAudioItems)

        result shouldBeEqualTo left(throwable)
        coVerifyOnce {
            repository.updatePlaylistItems(playlistId, playlistAudioItems)
        }
    }
}
