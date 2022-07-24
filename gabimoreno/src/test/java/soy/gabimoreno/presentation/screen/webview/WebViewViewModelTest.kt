package soy.gabimoreno.presentation.screen.webview

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.WEB_VIEW_URL
import soy.gabimoreno.data.tracker.main.WebViewTrackerEvent

class WebViewViewModelTest {

    private val tracker: Tracker = relaxedMockk()
    private lateinit var viewModel: WebViewViewModel

    @Before
    fun setUp() {
        viewModel = WebViewViewModel(
            tracker
        )
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() {
        val url = URL
        viewModel.onViewScreen(url)

        verifyOnce {
            tracker.trackEvent(
                withArg { event ->
                    event shouldBeInstanceOf WebViewTrackerEvent.ViewScreen::class.java
                    val parameters = event.parameters
                    parameters[WEB_VIEW_URL] shouldBe url
                }
            )
        }
    }

    @Test
    fun `WHEN onBackClicked THEN track event`() {
        val url = URL
        viewModel.onBackClicked(url)

        verifyOnce {
            tracker.trackEvent(
                withArg { event ->
                    event shouldBeInstanceOf WebViewTrackerEvent.ClickBack::class.java
                    val parameters = event.parameters
                    parameters[WEB_VIEW_URL] shouldBe url
                }
            )
        }
    }

    @Test
    fun `WHEN onPageStarted THEN track event`() {
        val url = URL
        viewModel.onPageStarted(url)

        verifyOnce {
            tracker.trackEvent(
                withArg { event ->
                    event shouldBeInstanceOf WebViewTrackerEvent.ViewPage::class.java
                    val parameters = event.parameters
                    parameters[WEB_VIEW_URL] shouldBe url
                }
            )
        }
    }
}

private const val URL = "https://foo.com"
