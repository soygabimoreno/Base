package soy.gabimoreno.presentation.screen.premium

import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent

class PremiumViewModelTest {

    private val tracker: Tracker = relaxedMockk()
    private lateinit var viewModel: PremiumViewModel

    @Before
    fun setUp() {
        viewModel = PremiumViewModel(
            tracker
        )
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() {
        viewModel.onViewScreen()

        verifyOnce {
            tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
        }
    }
}
