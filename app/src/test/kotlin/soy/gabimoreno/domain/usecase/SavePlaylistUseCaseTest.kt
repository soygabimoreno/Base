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
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.framework.datastore.getEmail

class SavePlaylistUseCaseTest {
    private val context: Context = mockk()
    private val playlistRepository = mockk<PlaylistRepository>()

    private lateinit var useCase: SavePlaylistUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = SavePlaylistUseCase(context, playlistRepository)
    }

    @Test
    fun `GIVEN valid playlist WHEN invoke THEN returns right with unit`() =
        runTest {
            val playlist = mockk<Playlist>()
            val expectedEither = Either.Right(Unit)
            coEvery { playlistRepository.savePlaylist(playlist, EMAIL) } returns expectedEither

            val result = useCase(playlist)

            result shouldBeEqualTo expectedEither
            coVerifyOnce {
                playlistRepository.savePlaylist(playlist, EMAIL)
            }
        }

    @Test
    fun `GIVEN repository throws exception WHEN invoke THEN returns left with throwable`() =
        runTest {
            val playlist = mockk<Playlist>()
            val exception = RuntimeException("Save failed")
            val expectedEither = Either.Left(exception)
            coEvery { playlistRepository.savePlaylist(playlist, EMAIL) } returns expectedEither

            val result = useCase(playlist)

            result shouldBeEqualTo expectedEither
            coVerifyOnce {
                playlistRepository.savePlaylist(playlist, EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
