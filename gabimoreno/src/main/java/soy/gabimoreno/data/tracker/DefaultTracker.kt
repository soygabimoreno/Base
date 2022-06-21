package soy.gabimoreno.data.tracker

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import soy.gabimoreno.framework.KLog
import soy.gabimoreno.framework.camelToSnakeUpperCase
import javax.inject.Inject

class DefaultTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : Tracker {

    override fun trackEvent(trackerEvent: TrackerEvent) {
        val name = trackerEvent.javaClass.simpleName.toString().camelToSnakeUpperCase()
        val parameters = trackerEvent.parameters
        val bundle = Bundle()
        parameters.forEach { entry: Map.Entry<String, Any> ->
            val (key, value) = entry
            when (value) {
                is Int -> bundle.putInt(key, value)
                is String -> bundle.putString(key, value)
                is Double -> bundle.putDouble(key, value)
                is Float -> bundle.putFloat(key, value)
                is Boolean -> bundle.putBoolean(key, value)
            }
        }
        firebaseAnalytics.logEvent(name, bundle)
        KLog.d("$name: $bundle")
    }
}
