package soy.gabimoreno.domain.usecase

import arrow.core.Either
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository

class ResetPlaylistByIdUseCaseTest {

    private val playlistRepository = mockk<PlaylistRepository>()
    private lateinit var useCase: ResetPlaylistByIdUseCase

    @Before
    fun setUp() {
        useCase = ResetPlaylistByIdUseCase(playlistRepository)
    }

    @Test
    fun `GIVEN valid playlist id WHEN invoke THEN returns right with unit`() = runTest {
        val playlistId = 123
        val expectedEither = Either.Right(Unit)
        coEvery { playlistRepository.resetPlaylistById(playlistId) } returns expectedEither

        val result = useCase(playlistId)

        result shouldBeEqualTo expectedEither
        coVerifyOnce {
            playlistRepository.resetPlaylistById(playlistId)
        }
    }

    @Test
    fun `GIVEN repository throws exception WHEN invoke THEN returns left with throwable`() =
        runTest {
            val playlistId = 123
            val exception = RuntimeException("Reset failed")
            val expectedEither = Either.Left(exception)
            coEvery { playlistRepository.resetPlaylistById(playlistId) } returns expectedEither

            val result = useCase(playlistId)

            result shouldBeEqualTo expectedEither
            coVerifyOnce {
                playlistRepository.resetPlaylistById(playlistId)
            }
        }

    @Test
    fun `GIVEN empty playlist id WHEN invoke THEN delegates to repository`() = runTest {
        val playlistId = -1
        val expectedEither = Either.Right(Unit)
        coEvery { playlistRepository.resetPlaylistById(playlistId) } returns expectedEither

        val result = useCase(playlistId)

        result shouldBeEqualTo expectedEither
        coVerifyOnce {
            playlistRepository.resetPlaylistById(playlistId)
        }
    }
}
