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
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.framework.datastore.getEmail

class ResetPlaylistByIdUseCaseTest {
    private val context: Context = mockk()
    private val playlistRepository = mockk<PlaylistRepository>()

    private lateinit var useCase: ResetPlaylistByIdUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = ResetPlaylistByIdUseCase(context, playlistRepository)
    }

    @Test
    fun `GIVEN valid playlist id WHEN invoke THEN returns right with unit`() =
        runTest {
            val playlistId = 123
            val expectedEither = Either.Right(Unit)
            coEvery { playlistRepository.resetPlaylistById(playlistId, EMAIL) } returns expectedEither

            val result = useCase(playlistId)

            result shouldBeEqualTo expectedEither
            coVerifyOnce {
                playlistRepository.resetPlaylistById(playlistId, EMAIL)
            }
        }

    @Test
    fun `GIVEN repository throws exception WHEN invoke THEN returns left with throwable`() =
        runTest {
            val playlistId = 123
            val exception = RuntimeException("Reset failed")
            val expectedEither = Either.Left(exception)
            coEvery {
                playlistRepository.resetPlaylistById(
                    playlistId,
                    EMAIL,
                )
            } returns expectedEither

            val result = useCase(playlistId)

            result shouldBeEqualTo expectedEither
            coVerifyOnce {
                playlistRepository.resetPlaylistById(playlistId, EMAIL)
            }
        }

    @Test
    fun `GIVEN empty playlist id WHEN invoke THEN delegates to repository`() =
        runTest {
            val playlistId = -1
            val expectedEither = Either.Right(Unit)
            coEvery { playlistRepository.resetPlaylistById(playlistId, EMAIL) } returns expectedEither

            val result = useCase(playlistId)

            result shouldBeEqualTo expectedEither
            coVerifyOnce {
                playlistRepository.resetPlaylistById(playlistId, EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
