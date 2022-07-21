package soy.gabimoreno.presentation.screen.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.EPISODE_ID
import soy.gabimoreno.data.tracker.domain.EPISODE_TITLE
import soy.gabimoreno.data.tracker.main.HomeTrackerEvent
import soy.gabimoreno.domain.repository.PodcastRepository
import soy.gabimoreno.domain.usecase.GetAppVersionNameUseCase

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val podcastRepository: PodcastRepository = relaxedMockk()
    private val tracker: Tracker = relaxedMockk()
    private val getAppVersionNameUseCase: GetAppVersionNameUseCase = relaxedMockk()
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(
            podcastRepository,
            tracker,
            getAppVersionNameUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN init THEN appVersionName is loaded`() {
        verifyOnce { getAppVersionNameUseCase() }
        viewModel.appVersionName.isNotBlank()
    }

    @Test
    fun `WHEN init THEN getEpisodes`() = runTest(testDispatcher) {
        viewModel.podcastSearch shouldBe HomeViewModel.ViewState.Loading
        coVerifyOnce { podcastRepository.getEpisodes() }
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() {
        viewModel.onViewScreen()

        verifyOnce { tracker.trackEvent(HomeTrackerEvent.ViewScreen) }
    }

    @Test
    fun `WHEN onEpisodeClicked THEN track event`() {
        val episodeId = "episodeId"
        val episodeTitle = "episodeTitle"
        viewModel.onEpisodeClicked(episodeId, episodeTitle)

        verifyOnce {
            tracker.trackEvent(
                HomeTrackerEvent.ClickEpisode(
                    mapOf(
                        EPISODE_ID to episodeId,
                        EPISODE_TITLE to episodeTitle
                    )
                )
            )
        }
    }
}
