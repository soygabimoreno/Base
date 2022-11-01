package soy.gabimoreno.framework.notification

import com.google.firebase.messaging.FirebaseMessagingService
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.framework.KLog
import javax.inject.Inject

class DefaultFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var tracker: Tracker

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        KLog.i("Refreshed token: $token")

        // TODO: Implement this ensuring lateinit var tracker is initialized
        // tracker.trackEvent(NotificationTrackerEvent.OnNewToken(mapOf(TRACKER_KEY_NEW_TOKEN to token)))
    }
}
