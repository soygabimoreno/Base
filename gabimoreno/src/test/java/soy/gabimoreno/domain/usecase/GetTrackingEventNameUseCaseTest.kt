package soy.gabimoreno.domain.usecase

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.data.tracker.main.HomeTrackerEvent

class GetTrackingEventNameUseCaseTest {

    private lateinit var useCase: GetTrackingEventNameUseCase

    @Before
    fun setUp() {
        useCase = GetTrackingEventNameUseCase()
    }

    @Test
    fun `GIVEN TrackerEvent WHEN invoke THEN get the expected text`() {
        val trackerEvent = HomeTrackerEvent.ViewScreen

        val result = useCase(trackerEvent)

        result shouldBeEqualTo "HOME_VIEW_SCREEN"
    }
}
