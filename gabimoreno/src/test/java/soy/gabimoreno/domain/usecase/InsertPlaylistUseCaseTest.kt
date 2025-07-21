package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.left
import arrow.core.right
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
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.framework.datastore.getEmail

class InsertPlaylistUseCaseTest {
    private val context: Context = mockk()
    private val playlistRepository = mockk<PlaylistRepository>()

    private lateinit var useCase: InsertPlaylistUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = InsertPlaylistUseCase(context, playlistRepository)
    }

    @Test
    fun `GIVEN valid parameters WHEN InsertPlaylistUseCase THEN returns right with playlist`() =
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
            coEvery {
                playlistRepository.insertPlaylist(name, description, EMAIL)
            } returns playList.right()

            val result = useCase(name, description)

            result shouldBeEqualTo playList.right()
            coVerifyOnce {
                playlistRepository.insertPlaylist(name, description, EMAIL)
            }
        }

    @Test
    fun `GIVEN repository throws exception WHEN invoke THEN returns left with throwable`() =
        runTest {
            val name = "Playlist Name"
            val description = "Playlist Description"
            val exception: Throwable = RuntimeException("Database error")
            coEvery {
                playlistRepository.insertPlaylist(name, description, EMAIL)
            } returns exception.left()

            val result = useCase(name, description)

            result shouldBeEqualTo exception.left()
            coVerifyOnce {
                playlistRepository.insertPlaylist(name, description, EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
