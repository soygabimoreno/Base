package soy.gabimoreno.domain.usecase

import arrow.core.Either
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository

class DeleteAllPlaylistUseCaseTest {

    private val playlistRepository: PlaylistRepository = relaxedMockk()
    private lateinit var useCase: DeleteAllPlaylistUseCase

    @Before
    fun setUp() {
        useCase = DeleteAllPlaylistUseCase(playlistRepository)
    }

    @Test
    fun `GIVEN repository returns Right WHEN invoke THEN returns Right with Unit`() = runTest {
        coEvery { playlistRepository.deleteAllPlaylists() } returns Either.Right(Unit)

        val result = useCase()

        result shouldBeInstanceOf Either.Right::class
        result.getOrNull() shouldBeEqualTo Unit
    }

    @Test
    fun `GIVEN repository returns Left WHEN invoke THEN returns Left with exception`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { playlistRepository.deleteAllPlaylists() } returns Either.Left(exception)

        val result = useCase()

        result shouldBeInstanceOf Either.Left::class
        result.leftOrNull() shouldBeEqualTo exception
    }

    @Test
    fun `GIVEN repository WHEN invoke THEN deleteAllPlaylists is called`() = runTest {
        coEvery { playlistRepository.deleteAllPlaylists() } returns Either.Right(Unit)

        useCase()

        coVerifyOnce {
            playlistRepository.deleteAllPlaylists()
        }
    }
}
