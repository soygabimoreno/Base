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
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.fake.buildPlaylist
import soy.gabimoreno.framework.datastore.getEmail

class GetAllPlaylistUseCaseTest {
    private val context: Context = mockk()
    private val playlistRepository: PlaylistRepository = relaxedMockk()

    private lateinit var useCase: GetAllPlaylistUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = GetAllPlaylistUseCase(context, playlistRepository)
    }

    @Test
    fun `GIVEN repository returns Right with playlists WHEN invoke THEN returns Right with playlists`() =
        runTest {
            val playlists = listOf(buildPlaylist())
            coEvery { playlistRepository.getAllPlaylists(EMAIL) } returns Either.Right(playlists)

            val result = useCase()

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo playlists
        }

    @Test
    fun `GIVEN repository returns Right with empty list WHEN invoke THEN returns Right with empty list`() =
        runTest {
            val emptyPlaylists = emptyList<Playlist>()
            coEvery { playlistRepository.getAllPlaylists(EMAIL) } returns
                Either.Right(
                    emptyPlaylists,
                )

            val result = useCase()

            result shouldBeInstanceOf Either.Right::class
            result.getOrNull() shouldBeEqualTo emptyPlaylists
        }

    @Test
    fun `GIVEN repository returns Left WHEN invoke THEN returns Left with exception`() =
        runTest {
            val exception = RuntimeException("Database error")
            coEvery { playlistRepository.getAllPlaylists(EMAIL) } returns Either.Left(exception)

            val result = useCase()

            result shouldBeInstanceOf Either.Left::class
            result.leftOrNull() shouldBeEqualTo exception
        }

    @Test
    fun `GIVEN repository WHEN invoke THEN getAllPlaylists is called`() =
        runTest {
            coEvery { playlistRepository.getAllPlaylists(EMAIL) } returns Either.Right(emptyList())

            useCase()

            coVerifyOnce {
                playlistRepository.getAllPlaylists(EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
