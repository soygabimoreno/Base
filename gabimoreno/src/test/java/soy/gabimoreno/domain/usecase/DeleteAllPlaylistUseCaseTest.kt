package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.Either
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.framework.datastore.getEmail

class DeleteAllPlaylistUseCaseTest {
    private val context: Context = mockk()
    private val playlistRepository: PlaylistRepository = relaxedMockk()

    private lateinit var useCase: DeleteAllPlaylistUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = DeleteAllPlaylistUseCase(context, playlistRepository)
    }

    @Test
    fun `GIVEN repository returns Right WHEN invoke THEN returns Right with Unit`() =
        runTest {
            coEvery { playlistRepository.deleteAllPlaylists(EMAIL) } returns Either.Right(Unit)

            val result = useCase()

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo Unit
        }

    @Test
    fun `GIVEN repository returns Left WHEN invoke THEN returns Left with exception`() =
        runTest {
            val exception = RuntimeException("Database error")
            coEvery { playlistRepository.deleteAllPlaylists(EMAIL) } returns Either.Left(exception)

            val result = useCase()

            result shouldBeInstanceOf Either.Left::class
            result.leftOrNull() shouldBeEqualTo exception
        }

    @Test
    fun `GIVEN repository WHEN invoke THEN deleteAllPlaylists is called`() =
        runTest {
            coEvery { playlistRepository.deleteAllPlaylists(EMAIL) } returns Either.Right(Unit)

            useCase()

            coVerifyOnce {
                playlistRepository.deleteAllPlaylists(EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
