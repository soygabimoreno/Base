package soy.gabimoreno.domain.usecase

import arrow.core.Either
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository

class SavePlaylistUseCaseTest {

    private val playlistRepository = mockk<PlaylistRepository>()
    private lateinit var useCase: SavePlaylistUseCase

    @Before
    fun setUp() {
        useCase = SavePlaylistUseCase(playlistRepository)
    }

    @Test
    fun `GIVEN valid playlist WHEN invoke THEN returns right with unit`() = runTest {
        val playlist = mockk<Playlist>()
        val expectedEither = Either.Right(Unit)
        coEvery { playlistRepository.savePlaylist(playlist) } returns expectedEither

        val result = useCase(playlist)

        result shouldBeEqualTo expectedEither
        coVerifyOnce {
            playlistRepository.savePlaylist(playlist)
        }
    }

    @Test
    fun `GIVEN repository throws exception WHEN invoke THEN returns left with throwable`() =
        runTest {
            val playlist = mockk<Playlist>()
            val exception = RuntimeException("Save failed")
            val expectedEither = Either.Left(exception)
            coEvery { playlistRepository.savePlaylist(playlist) } returns expectedEither

            val result = useCase(playlist)

            result shouldBeEqualTo expectedEither
            coVerifyOnce {
                playlistRepository.savePlaylist(playlist)
            }
        }
}
