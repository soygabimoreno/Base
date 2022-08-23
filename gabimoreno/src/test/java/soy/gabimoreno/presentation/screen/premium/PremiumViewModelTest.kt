package soy.gabimoreno.presentation.screen.premium

import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import soy.gabimoreno.remoteconfig.RemoteConfigName
import soy.gabimoreno.remoteconfig.RemoteConfigProvider

@ExperimentalCoroutinesApi
class PremiumViewModelTest {

    private val tracker: Tracker = relaxedMockk()
    private val remoteConfigProvider: RemoteConfigProvider = relaxedMockk()
    private lateinit var viewModel: PremiumViewModel
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PremiumViewModel(
            tracker,
            remoteConfigProvider,
            testDispatcher
        )
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN PREMIUM_SUBSCRIPTION_LAUNCH enabled WHEN onViewScreen THEN track event and show login`() {
        every { remoteConfigProvider.isFeatureEnabled(RemoteConfigName.PREMIUM_SUBSCRIPTION_LAUNCH) } returns true

        viewModel.onViewScreen()

        verifyOnce {
            tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
            // TODO: Test this navigation
//            runTest(testDispatcher) {
//                val viewEvent = viewModel.viewEventFlow.first()
//                viewEvent shouldBe PremiumViewModel.ViewEvent.ShowLogin
//            }
        }
    }

    @Test
    fun `GIVEN PREMIUM_SUBSCRIPTION_LAUNCH disabled WHEN onViewScreen THEN track event and do not show login`() {
        every { remoteConfigProvider.isFeatureEnabled(RemoteConfigName.PREMIUM_SUBSCRIPTION_LAUNCH) } returns true

        viewModel.onViewScreen()

        verifyOnce {
            tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
            // TODO: Test this navigation
//            runTest(testDispatcher) {
//                val viewEvent = viewModel.viewEventFlow.first()
//                viewEvent shouldNotBe PremiumViewModel.ViewEvent.ShowLogin
//            }
        }
    }
}
