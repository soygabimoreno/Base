package soy.gabimoreno.domain.usecase

import com.google.firebase.messaging.FirebaseMessaging
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker

class SubscribeToTopicUseCaseTest {

    private val firebaseMessaging: FirebaseMessaging = relaxedMockk()
    private val tracker: Tracker = relaxedMockk()
    private lateinit var useCase: SubscribeToTopicUseCase

    @Before
    fun setUp() {
        useCase = SubscribeToTopicUseCase(
            firebaseMessaging,
            tracker
        )
    }

    @Test
    fun `GIVEN a topic WHEN invoke THEN subscribe to that topic`() {
        val topic = "topic"

        useCase(topic)

        verifyOnce { firebaseMessaging.subscribeToTopic(topic) }

        // TODO: Missed test the tracking events
    }
}
