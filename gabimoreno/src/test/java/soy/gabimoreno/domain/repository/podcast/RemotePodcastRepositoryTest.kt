package soy.gabimoreno.domain.repository.podcast

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.fake.buildEpisodesWrapper

@ExperimentalCoroutinesApi
class RemotePodcastRepositoryTest {

    private val podcastDatasource: PodcastDatasource = mockk()
    private val podcastUrl: PodcastUrl = "podcastUrl"

    private lateinit var repository: RemotePodcastRepository

    @Before
    fun setUp() {
        repository = RemotePodcastRepository(
            podcastDatasource,
            podcastUrl
        )
    }

    @Test
    fun `GIVEN successful response WHEN getEpisodes THEN get the expected result`() = runTest {
        val episodesWrapper = buildEpisodesWrapper()
        coEvery { podcastDatasource.getEpisodes(podcastUrl) } returns episodesWrapper.right()

        val result = repository.getEpisodes()

        coVerifyOnce {
            podcastDatasource.getEpisodes(podcastUrl)
        }
        result shouldBeEqualTo episodesWrapper.right()
    }

    @Test
    fun `GIVEN failure response WHEN getEpisodes THEN get the expected error`() = runTest {
        val throwable = Throwable()
        coEvery { podcastDatasource.getEpisodes(podcastUrl) } returns throwable.left()

        val result = repository.getEpisodes()

        coVerifyOnce {
            podcastDatasource.getEpisodes(podcastUrl)
        }
        result shouldBeEqualTo throwable.left()
    }
}
