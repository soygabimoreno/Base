package soy.gabimoreno.data.tracker

import com.google.firebase.analytics.FirebaseAnalytics
import soy.gabimoreno.domain.usecase.GetTrackingEventNameUseCase
import soy.gabimoreno.framework.KLog
import soy.gabimoreno.framework.mapToBundle
import javax.inject.Inject

class DefaultTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val getTrackingEventNameUseCase: GetTrackingEventNameUseCase
) : Tracker {

    override fun trackEvent(trackerEvent: TrackerEvent) {
        val name = getTrackingEventNameUseCase(trackerEvent)
        val parameters = trackerEvent.parameters
        val bundle = parameters.mapToBundle()
        firebaseAnalytics.logEvent(name, bundle)
        KLog.d("$name: $bundle")
    }
}
