package soy.gabimoreno.domain.usecase

import com.google.firebase.messaging.FirebaseMessaging
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_NOTIFICATION_SUBSCRIBED
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_NOTIFICATION_TOPIC
import soy.gabimoreno.data.tracker.main.NotificationTrackerEvent
import javax.inject.Inject

class SubscribeToTopicUseCase @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val tracker: Tracker,
) {
    operator fun invoke(topic: String) {
        firebaseMessaging
            .subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                val isSuccessful = task.isSuccessful
                tracker.trackEvent(
                    NotificationTrackerEvent.OnSubscribedToTopic(
                        mapOf(
                            TRACKER_KEY_NOTIFICATION_TOPIC to topic,
                            TRACKER_KEY_NOTIFICATION_SUBSCRIBED to isSuccessful
                        )
                    )
                )
            }
    }
}
