@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.data.repository.podcast

import arrow.core.Either
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.data.cloud.audiosync.datasource.PodcastCloudDataSource
import soy.gabimoreno.data.local.podcast.LocalPodcastDataSource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildCloudPodcastResponse
import soy.gabimoreno.fake.buildEpisode

class DefaultPodcastRepositoryTest {
    private val cloudDataSource: PodcastCloudDataSource = mockk()
    private val localPodcastDatasource: LocalPodcastDataSource = mockk()
    private val remotePodcastDatasource: PodcastDatasource = mockk()

    private lateinit var repository: DefaultPodcastRepository

    @Test
    fun `GIVEN remote returns Right and email is not empty WHEN getEpisodesStream THEN local flow is returned and cloud applied merge`() =
        runTest {
            repository = buildRepository()
            val episodeId = "ABC"
            val remoteEpisodes =
                listOf(
                    buildEpisode(id = episodeId, hasBeenListened = false, markedAsFavorite = false),
                )
            val cloudEpisodes =
                listOf(
                    buildCloudPodcastResponse(id = episodeId, hasBeenListened = true),
                )
            coEvery { remotePodcastDatasource.getEpisodesStream(PODCAST_URL) } returns right(flowOf(remoteEpisodes))
            coEvery { localPodcastDatasource.getPodcasts() } returns flowOf(remoteEpisodes)
            coEvery { cloudDataSource.getPodcastItems(EMAIL) } returns cloudEpisodes
            coJustRun { localPodcastDatasource.upsertPodcasts(any()) }

            val result = repository.getEpisodesStream(EMAIL)
            advanceUntilIdle()

            result shouldBeInstanceOf Either.Right::class

            coVerifyOnce {
                remotePodcastDatasource.getEpisodesStream(PODCAST_URL)
                cloudDataSource.getPodcastItems(EMAIL)
                localPodcastDatasource.upsertPodcasts(any())
                localPodcastDatasource.getPodcasts()
            }
        }

    @Test
    fun `GIVEN remote returns Right and email is empty WHEN getEpisodesStream THEN local flow is returned and local merge is applied`() =
        runTest {
            val emptyEmail = ""
            repository = buildRepository()
            val remoteEpisodes = listOf(buildEpisode())
            val localEpisodes = listOf(buildEpisode(hasBeenListened = true, markedAsFavorite = true))
            coEvery { remotePodcastDatasource.getEpisodesStream(PODCAST_URL) } returns right(flowOf(remoteEpisodes))
            coEvery { localPodcastDatasource.getPodcasts() } returns flowOf(localEpisodes)
            coJustRun { localPodcastDatasource.upsertPodcasts(any()) }

            val result = repository.getEpisodesStream(emptyEmail)
            advanceUntilIdle()

            result shouldBeInstanceOf Either.Right::class
            coVerify {
                localPodcastDatasource.getPodcasts()
            }
            coVerifyOnce {
                remotePodcastDatasource.getEpisodesStream(PODCAST_URL)
                localPodcastDatasource.upsertPodcasts(any())
            }
            coVerifyNever {
                cloudDataSource.getPodcastItems(any())
            }
        }

    @Test
    fun `GIVEN remote returns Left WHEN getEpisodesStream THEN local flow is returned without merge`() =
        runTest {
            repository = buildRepository()
            coEvery { remotePodcastDatasource.getEpisodesStream(PODCAST_URL) } returns left(Throwable())
            coEvery { localPodcastDatasource.getPodcasts() } returns flowOf(emptyList())

            val result = repository.getEpisodesStream(EMAIL)
            advanceUntilIdle()

            result shouldBeInstanceOf Either.Right::class
            coVerifyOnce {
                remotePodcastDatasource.getEpisodesStream(PODCAST_URL)
                localPodcastDatasource.getPodcasts()
            }
            coVerifyNever {
                localPodcastDatasource.upsertPodcasts(any())
                cloudDataSource.getPodcastItems(any())
            }
        }

    @Test
    fun `GIVEN podcast exists in local WHEN getPodcastById THEN podcast is returned`() =
        runTest {
            val episodeId = "id"
            repository = buildRepository()
            val episode = buildEpisode()
            coEvery { localPodcastDatasource.getPodcastById(episodeId) } returns episode

            val result = repository.getPodcastById(episodeId)

            result shouldBeEqualTo right(episode)
            coVerifyOnce { localPodcastDatasource.getPodcastById(episodeId) }
        }

    @Test
    fun `GIVEN podcast does not exist in local WHEN getPodcastById THEN error is returned`() =
        runTest {
            val episodeId = "id"
            repository = buildRepository()
            coEvery { localPodcastDatasource.getPodcastById(episodeId) } returns null

            val result = repository.getPodcastById(episodeId)

            result shouldBeInstanceOf Either.Left::class
            coVerifyOnce { localPodcastDatasource.getPodcastById(episodeId) }
        }

    @Test
    fun `WHEN getTotalPodcasts THEN total is returned`() =
        runTest {
            val totalReturned = 5
            repository = buildRepository()
            coEvery { localPodcastDatasource.getTotalPodcasts() } returns totalReturned

            val result = repository.getTotalPodcasts()

            result shouldBeEqualTo right(totalReturned)
            coVerifyOnce { localPodcastDatasource.getTotalPodcasts() }
        }

    @Test
    fun `WHEN isEmpty THEN boolean is returned`() =
        runTest {
            repository = buildRepository()

            coEvery { localPodcastDatasource.isEmpty() } returns true

            val result = repository.isEmpty()

            result shouldBeEqualTo right(true)
            coVerifyOnce { localPodcastDatasource.isEmpty() }
        }

    @Test
    fun `GIVEN email is not empty WHEN markAllPodcastAsUnlistened THEN both cloud and local are updated`() =
        runTest {
            repository = buildRepository()
            coJustRun { cloudDataSource.batchUpdateFieldsForAllPodcastItems(EMAIL, any()) }
            coJustRun { localPodcastDatasource.markAllPodcastAsUnlistened() }

            repository.markAllPodcastAsUnlistened(EMAIL)

            coVerifyOnce {
                cloudDataSource.batchUpdateFieldsForAllPodcastItems(EMAIL, any())
                localPodcastDatasource.markAllPodcastAsUnlistened()
            }
        }

    @Test
    fun `GIVEN email is empty WHEN markAllPodcastAsUnlistened THEN only local is updated`() =
        runTest {
            val emptyEmail = ""
            repository = buildRepository()
            coJustRun { localPodcastDatasource.markAllPodcastAsUnlistened() }

            repository.markAllPodcastAsUnlistened(emptyEmail)

            coVerifyOnce {
                localPodcastDatasource.markAllPodcastAsUnlistened()
            }
            coVerifyNever {
                cloudDataSource.batchUpdateFieldsForAllPodcastItems(any(), any())
            }
        }

    @Test
    fun `GIVEN email is not empty WHEN markPodcastAsListened enabled THEN both cloud and local are updated`() =
        runTest {
            val podcastId = "id"
            repository = buildRepository()
            coJustRun { cloudDataSource.upsertPodcastFields(EMAIL, podcastId, any()) }
            coJustRun { localPodcastDatasource.updateHasBeenListened(podcastId, true) }

            repository.markPodcastAsListened(podcastId, EMAIL, true)

            coVerifyOnce {
                cloudDataSource.upsertPodcastFields(EMAIL, podcastId, any())
                localPodcastDatasource.updateHasBeenListened(podcastId, true)
            }
        }

    @Test
    fun `GIVEN email is empty WHEN markPodcastAsListened disabled THEN only local is updated`() =
        runTest {
            val emptyEmail = ""
            val podcastId = "id"
            repository = buildRepository()
            coJustRun { localPodcastDatasource.updateHasBeenListened(podcastId, false) }

            repository.markPodcastAsListened(podcastId, emptyEmail, false)

            coVerifyOnce {
                localPodcastDatasource.updateHasBeenListened(podcastId, false)
            }
            coVerifyNever {
                cloudDataSource.upsertPodcastFields(any(), any(), any())
            }
        }

    @Test
    fun `WHEN reset THEN local is reset`() =
        runTest {
            repository = buildRepository()
            coJustRun { localPodcastDatasource.reset() }

            repository.reset()

            coVerifyOnce {
                localPodcastDatasource.reset()
            }
        }

    @Test
    fun `GIVEN email is not empty WHEN updateMarkedAsFavorite enabled THEN both cloud and local are updated`() =
        runTest {
            val podcastId = "id"
            repository = buildRepository()
            coJustRun { cloudDataSource.upsertPodcastFields(EMAIL, podcastId, any()) }
            coJustRun { localPodcastDatasource.updateMarkedAsFavorite(podcastId, true) }

            repository.updateMarkedAsFavorite(podcastId, EMAIL, true)

            coVerifyOnce {
                cloudDataSource.upsertPodcastFields(EMAIL, podcastId, any())
                localPodcastDatasource.updateMarkedAsFavorite(podcastId, true)
            }
        }

    @Test
    fun `GIVEN email is empty WHEN updateMarkedAsFavorite disabled THEN only local is updated`() =
        runTest {
            val emptyEmail = ""
            val podcastId = "id"
            repository = buildRepository()
            coJustRun { localPodcastDatasource.updateMarkedAsFavorite(podcastId, false) }

            repository.updateMarkedAsFavorite(podcastId, emptyEmail, false)

            coVerifyOnce {
                localPodcastDatasource.updateMarkedAsFavorite(podcastId, false)
            }
            coVerifyNever {
                cloudDataSource.upsertPodcastFields(any(), any(), any())
            }
        }

    private fun TestScope.buildRepository(): DefaultPodcastRepository =
        DefaultPodcastRepository(
            cloudDataSource,
            localPodcastDatasource,
            PODCAST_URL,
            remotePodcastDatasource,
            this,
        )
}

private const val EMAIL: String = "test@test.es"
private const val PODCAST_URL: String = "url"
