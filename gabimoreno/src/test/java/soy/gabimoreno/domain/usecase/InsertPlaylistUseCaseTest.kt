package soy.gabimoreno.domain.usecase

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository

class InsertPlaylistUseCaseTest {

    private val playlistRepository = mockk<PlaylistRepository>()
    private lateinit var useCase: InsertPlaylistUseCase

    @Before
    fun setUp() {
        useCase = InsertPlaylistUseCase(playlistRepository)
    }

    @Test
    fun `GIVEN valid parameters WHEN InsertPlaylistUseCase THEN returns right with playlist`() =
        runTest {
            val name = "Playlist Name"
            val description = "Playlist Description"
            val playList = Playlist(
                id = 1,
                title = name,
                description = description,
                items = emptyList(),
                position = 0
            )
            coEvery {
                playlistRepository.insertPlaylist(name, description)
            } returns playList.right()

            val result = useCase(name, description)

            result shouldBeEqualTo playList.right()
            coVerifyOnce {
                playlistRepository.insertPlaylist(name, description)
            }
        }

    @Test
    fun `GIVEN repository throws exception WHEN invoke THEN returns left with throwable`() =
        runTest {
            val name = "Playlist Name"
            val description = "Playlist Description"
            val exception: Throwable = RuntimeException("Database error")
            coEvery {
                playlistRepository.insertPlaylist(name, description)
            } returns exception.left()

            val result = useCase(name, description)

            result shouldBeEqualTo exception.left()
            coVerifyOnce {
                playlistRepository.insertPlaylist(name, description)
            }
        }
}
