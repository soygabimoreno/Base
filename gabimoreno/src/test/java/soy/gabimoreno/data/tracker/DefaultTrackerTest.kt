package soy.gabimoreno.data.tracker

import com.google.firebase.analytics.FirebaseAnalytics
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.domain.usecase.GetTrackingEventNameUseCase

@RunWith(RobolectricTestRunner::class)
class DefaultTrackerTest {

    private val firebaseAnalytics: FirebaseAnalytics = relaxedMockk()
    private val getTrackingEventNameUseCase: GetTrackingEventNameUseCase = relaxedMockk()
    private lateinit var defaultTracker: DefaultTracker

    @Before
    fun setUp() {
        defaultTracker = DefaultTracker(
            firebaseAnalytics,
            getTrackingEventNameUseCase
        )
    }

    @Test
    fun `WHEN trackEvent THEN do the corresponding calls`() {
        val trackerEvent: TrackerEvent = relaxedMockk()

        defaultTracker.trackEvent(trackerEvent)

        verifyOnce {
            getTrackingEventNameUseCase(trackerEvent)
            firebaseAnalytics.logEvent(any(), any())
        }
    }
}
