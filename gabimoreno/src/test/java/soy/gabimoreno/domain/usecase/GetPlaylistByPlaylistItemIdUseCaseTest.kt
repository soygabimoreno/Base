package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildPlaylist

class GetPlaylistByPlaylistItemIdUseCaseTest {

    private val repository: PlaylistRepository = relaxedMockk()
    private lateinit var useCase: GetPlaylistByPlaylistItemIdUseCase

    @Before
    fun setUp() {
        useCase = GetPlaylistByPlaylistItemIdUseCase(repository)
    }

    @Test
    fun `GIVEN valid playlist item id WHEN invoke THEN returns right with playlist ids`() =
        runTest {
            val playlist1 = buildPlaylist()
            val playlist2 = buildPlaylist(2)
            val playlistIds = listOf(playlist1.id, playlist2.id)
            coEvery {
                repository.getPlaylistIdsByItemId(playlist1.items.first().id)
            } returns right(playlistIds)

            val result = useCase(playlist1.items.first().id)

            result shouldBeEqualTo right(playlistIds)
            coVerifyOnce {
                repository.getPlaylistIdsByItemId(playlist1.items.first().id)
            }
        }
}
