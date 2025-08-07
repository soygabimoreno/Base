@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.data.repository.podcast

import arrow.core.Either
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.data.local.podcast.LocalPodcastDataSource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildEpisode
import soy.gabimoreno.fake.buildEpisodes

class DefaultPodcastRepositoryTest {
    private val localPodcastDatasource: LocalPodcastDataSource = mockk()
    private val remotePodcastDatasource: PodcastDatasource = mockk()
    private val scope = TestScope()

    private lateinit var repository: DefaultPodcastRepository

    @Before
    fun setUp() {
        repository =
            DefaultPodcastRepository(
                localPodcastDatasource,
                PODCAST_URL,
                remotePodcastDatasource,
                scope,
            )
    }

    @Test
    fun `GIVEN remote returns Right WHEN getEpisodesStream THEN local flow is returned`() =
        runTest {
            val remoteEpisodes = buildEpisodes()
            val remoteFlow = flowOf(remoteEpisodes)
            val localFlow = flowOf(listOf(buildEpisode()))
            coEvery { remotePodcastDatasource.getEpisodesStream(PODCAST_URL) } returns right(remoteFlow)
            coEvery { localPodcastDatasource.getPodcasts() } returns localFlow

            val result = repository.getEpisodesStream()
            advanceUntilIdle()

            result shouldBeInstanceOf Either.Right::class
            coVerifyOnce {
                remotePodcastDatasource.getEpisodesStream(PODCAST_URL)
                localPodcastDatasource.getPodcasts()
            }
        }
}

private const val PODCAST_URL: String = "url"
