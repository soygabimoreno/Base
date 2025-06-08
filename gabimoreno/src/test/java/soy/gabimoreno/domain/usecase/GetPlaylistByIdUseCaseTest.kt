package soy.gabimoreno.domain.usecase

import arrow.core.Either
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.fake.buildPlaylist

class GetPlaylistByIdUseCaseTest {

    private val playlistRepository: PlaylistRepository = relaxedMockk()
    private lateinit var useCase: GetPlaylistByIdUseCase

    @Before
    fun setUp() {
        useCase = GetPlaylistByIdUseCase(playlistRepository)
    }

    @Test
    fun `GIVEN valid playlist id WHEN invoke THEN returns right with playlist flow`() {
        val playlist = listOf(buildPlaylist()).first()
        val playlistFlow = flowOf(playlist)
        val expectedEither = Either.Right(playlistFlow)
        every { playlistRepository.getPlaylistById(playlist.id) } returns expectedEither

        val result = useCase(playlist.id)

        result shouldBeEqualTo expectedEither
        verifyOnce {
            playlistRepository.getPlaylistById(playlist.id)
        }
    }

    @Test
    fun `GIVEN repository throws exception WHEN invoke THEN returns left with throwable`() {
        val playlistId = 123
        val exception = RuntimeException("Database error")
        val expectedEither = Either.Left(exception)
        every { playlistRepository.getPlaylistById(playlistId) } returns expectedEither

        val result = useCase(playlistId)

        result shouldBeEqualTo expectedEither
        verifyOnce {
            playlistRepository.getPlaylistById(playlistId)
        }
    }

    @Test
    fun `GIVEN empty playlistId WHEN invoke THEN delegates to repository`() {
        val playlistId = -1
        val playlistFlow = flowOf(null)
        val expectedEither = Either.Right(playlistFlow)
        every { playlistRepository.getPlaylistById(playlistId) } returns expectedEither

        val result = useCase(playlistId)

        result shouldBeEqualTo expectedEither
        verifyOnce {
            playlistRepository.getPlaylistById(playlistId)
        }
    }
}
