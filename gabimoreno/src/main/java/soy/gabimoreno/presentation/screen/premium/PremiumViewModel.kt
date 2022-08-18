package soy.gabimoreno.presentation.screen.premium

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import soy.gabimoreno.framework.KLog
import soy.gabimoreno.remoteconfig.RemoteConfigName
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val tracker: Tracker,
    private val remoteConfigProvider: RemoteConfigProvider
) : ViewModel() {

    fun onViewScreen() {
        tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
        if (remoteConfigProvider.isFeatureEnabled(RemoteConfigName.PREMIUM_SUBSCRIPTION_LAUNCH)) {
            KLog.d("FOO: enabled")
        } else {
            KLog.d("FOO: disabled")
        }
    }
}
