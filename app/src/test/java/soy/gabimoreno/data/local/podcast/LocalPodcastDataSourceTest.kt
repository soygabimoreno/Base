@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.data.local.podcast

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.local.ApplicationDatabase
import soy.gabimoreno.data.local.mapper.toPodcastDbModel
import soy.gabimoreno.data.local.podcast.dao.PodcastDbModelDao
import soy.gabimoreno.fake.buildEpisodes
import soy.gabimoreno.fake.buildPodcastDbModels

class LocalPodcastDataSourceTest {
    private val podcastDbModelDao: PodcastDbModelDao = mockk()
    private val database: ApplicationDatabase = mockk()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var datasource: LocalPodcastDataSource

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { database.podcastDbModelDao() } returns podcastDbModelDao
        datasource =
            LocalPodcastDataSource(
                database,
                testDispatcher,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN zeroCount WHEN isEmpty THEN returns true`() =
        runTest {
            coEvery { podcastDbModelDao.count() } returns 0

            val result = datasource.isEmpty()

            result shouldBe true
            coVerifyOnce {
                podcastDbModelDao.count()
            }
        }

    @Test
    fun `GIVEN nonZeroCount WHEN isEmpty THEN returns False`() =
        runTest {
            coEvery { podcastDbModelDao.count() } returns 3

            val result = datasource.isEmpty()

            result shouldBe false
            coVerifyOnce {
                podcastDbModelDao.count()
            }
        }

    @Test
    fun `GIVEN podcastCount WHEN getTotalPodcasts THEN returnsCount`() =
        runTest {
            val totalPodcasts = 42
            coEvery { podcastDbModelDao.count() } returns totalPodcasts

            val result = datasource.getTotalPodcasts()

            result shouldBeEqualTo totalPodcasts
            coVerifyOnce {
                podcastDbModelDao.count()
            }
        }

    @Test
    fun `GIVEN dbModels WHEN getPodcasts THEN returns MappedEpisodes`() =
        runTest {
            val podcastDbModels = buildPodcastDbModels()
            every { podcastDbModelDao.getPodcastDbModels() } returns flowOf(podcastDbModels)

            val result = datasource.getPodcasts().first()

            result.size shouldBeEqualTo podcastDbModels.size
            verifyOnce {
                podcastDbModelDao.getPodcastDbModels()
            }
        }

    @Test
    fun `GIVEN matchingId WHEN getPodcastById THEN returnsMappedEpisode`() =
        runTest {
            val podcastDbModels = buildPodcastDbModels()
            val podcastDbModel = podcastDbModels.first()
            coEvery { podcastDbModelDao.getPodcastDbModelById(podcastDbModel.id) } returns podcastDbModel

            val result = datasource.getPodcastById(podcastDbModel.id)

            result shouldNotBeEqualTo null
            coVerifyOnce {
                podcastDbModelDao.getPodcastDbModelById(podcastDbModel.id)
            }
        }

    @Test
    fun `GIVEN noMatch WHEN getPodcastById THEN returnsNull`() =
        runTest {
            val podcastId = "99"
            coEvery { podcastDbModelDao.getPodcastDbModelById(podcastId) } returns null

            val result = datasource.getPodcastById(podcastId)

            result shouldBe null
            coVerifyOnce {
                podcastDbModelDao.getPodcastDbModelById(podcastId)
            }
        }

    @Test
    fun `GIVEN episodes WHEN savePodcasts THEN savesMappedModels`() =
        runTest {
            val episodes = buildEpisodes()
            val podcastDbModels = episodes.map { it.toPodcastDbModel() }
            coJustRun { podcastDbModelDao.upsertPodcastDbModels(podcastDbModels) }

            datasource.upsertPodcasts(episodes)

            coVerifyOnce {
                podcastDbModelDao.upsertPodcastDbModels(podcastDbModels)
            }
        }

    @Test
    fun `WHEN reset THEN deletesAll`() =
        runTest {
            coJustRun { podcastDbModelDao.deleteAllPodcastDbModels() }

            datasource.reset()

            coVerifyOnce {
                podcastDbModelDao.deleteAllPodcastDbModels()
            }
        }

    @Test
    fun `GIVEN hasBeenListened true WHEN updateHasBeenListened THEN updatesValue`() =
        runTest {
            val podcastId = "abc"
            val hasBeenListened = true
            coJustRun { podcastDbModelDao.updateHasBeenListened(podcastId, hasBeenListened) }

            datasource.updateHasBeenListened(podcastId, hasBeenListened)

            coVerifyOnce {
                podcastDbModelDao.updateHasBeenListened(podcastId, hasBeenListened)
            }
        }

    @Test
    fun `GIVEN hasBeenListened false WHEN updateHasBeenListened THEN updatesValue`() =
        runTest {
            val podcastId = "abc"
            val hasBeenListened = false
            coJustRun { podcastDbModelDao.updateHasBeenListened(podcastId, hasBeenListened) }

            datasource.updateHasBeenListened(podcastId, hasBeenListened)

            coVerifyOnce {
                podcastDbModelDao.updateHasBeenListened(podcastId, hasBeenListened)
            }
        }

    @Test
    fun `GIVEN markedAsFavorite True WHEN updateMarkedAsFavorite THEN updatesValue`() =
        runTest {
            val podcastId = "abc"
            val markedAsFavorite = true
            coJustRun { podcastDbModelDao.updateMarkedAsFavorite(podcastId, markedAsFavorite) }

            datasource.updateMarkedAsFavorite(podcastId, markedAsFavorite)

            coVerifyOnce {
                podcastDbModelDao.updateMarkedAsFavorite(podcastId, markedAsFavorite)
            }
        }

    @Test
    fun `GIVEN markedAsFavorite False WHEN updateMarkedAsFavorite THEN updatesValue`() =
        runTest {
            val podcastId = "abc"
            val markedAsFavorite = false
            coJustRun { podcastDbModelDao.updateMarkedAsFavorite(podcastId, markedAsFavorite) }

            datasource.updateMarkedAsFavorite(podcastId, markedAsFavorite)

            coVerifyOnce {
                podcastDbModelDao.updateMarkedAsFavorite(podcastId, markedAsFavorite)
            }
        }
}
