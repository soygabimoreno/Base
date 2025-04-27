package soy.gabimoreno.domain.repository.podcast

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.data.remote.mapper.toDomain
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.fake.buildChannel
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
    fun `GIVEN successful response WHEN getEpisodesFlow THEN get the expected result`() = runTest {
        val channel = buildChannel(podcastUrl)
        val episodesWrapper = channel.toDomain()
        coEvery { podcastDatasource.getEpisodesStream(podcastUrl) } returns Either.Right(
            flowOf(episodesWrapper.episodes)
        )

        val result = repository.getEpisodesStream()

        result.isRight() shouldBe true
        val episodes = mutableListOf<List<Episode>>()
        result.onRight { flow ->
            flow.toList(episodes)
        }
        episodes shouldBeEqualTo episodesWrapper.episodes.chunked(15)
        coVerifyOnce { podcastDatasource.getEpisodesStream(podcastUrl) }
    }

    @Test
    fun `GIVEN failure response WHEN getEpisodesFlow THEN get the expected error`()= runTest {
        val throwable = Throwable()
        coEvery { podcastDatasource.getEpisodesStream(podcastUrl) } returns throwable.left()

        val result = repository.getEpisodesStream()

        coVerifyOnce {
            podcastDatasource.getEpisodesStream(podcastUrl)
        }
        result shouldBeEqualTo throwable.left()
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
