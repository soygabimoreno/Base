package soy.gabimoreno.presentation.screen.webview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_WEB_VIEW_URL
import soy.gabimoreno.data.tracker.main.WebViewTrackerEvent
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val tracker: Tracker,
) : ViewModel() {

    fun onViewScreen(url: String) {
        tracker.trackEvent(WebViewTrackerEvent.ViewScreen(url.toMap()))
    }

    fun onBackClicked(url: String) {
        tracker.trackEvent(WebViewTrackerEvent.ClickBack(url.toMap()))
    }

    fun onPageStarted(url: String) {
        tracker.trackEvent(WebViewTrackerEvent.ViewPage(url.toMap()))
    }
}

private fun String.toMap() = mapOf(
    TRACKER_KEY_WEB_VIEW_URL to this
)
