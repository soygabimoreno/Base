package soy.gabimoreno.presentation.screen.home

import io.mockk.mockk
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
import soy.gabimoreno.domain.repository.PodcastRepository
import soy.gabimoreno.domain.usecase.GetAppVersionNameUseCase

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val podcastRepository: PodcastRepository = mockk()
    private val tracker: Tracker = mockk()
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
        viewModel.podcastSearch shouldBe soy.gabimoreno.util.Resource.Loading
        coVerifyOnce { podcastRepository.getEpisodes() }
    }
}
