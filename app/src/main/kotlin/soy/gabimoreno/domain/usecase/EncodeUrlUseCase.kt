package soy.gabimoreno.domain.usecase

import androidx.annotation.VisibleForTesting
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EXCEPTION
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_URL
import soy.gabimoreno.data.tracker.main.ErrorTrackerEvent
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class EncodeUrlUseCase
    @Inject
    constructor(
        private val tracker: Tracker,
    ) {
        operator fun invoke(url: String): String =
            try {
                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            } catch (e: Exception) {
                tracker.trackEvent(
                    ErrorTrackerEvent.EncodeUrlException(
                        mapOf(
                            TRACKER_KEY_URL to url,
                            TRACKER_KEY_EXCEPTION to e.toString(),
                        ),
                    ),
                )
                GABI_MORENO_WEB_BASE_URL_ENCODED
            }
    }

@VisibleForTesting
internal const val GABI_MORENO_WEB_BASE_URL_ENCODED = "https%3A%2F%2Fgabimoreno.soy"
