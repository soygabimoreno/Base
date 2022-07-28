package soy.gabimoreno.presentation.screen.premium

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val tracker: Tracker
) : ViewModel() {

    fun onViewScreen() {
        tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
    }
}
