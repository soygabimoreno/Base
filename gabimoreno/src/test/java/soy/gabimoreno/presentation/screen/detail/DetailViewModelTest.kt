package soy.gabimoreno.presentation.screen.detail

import android.content.Context
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.R
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_AUDIO_ID
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_AUDIO_TITLE
import soy.gabimoreno.data.tracker.main.DetailTrackerEvent
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.fake.buildAudio
import soy.gabimoreno.framework.intent.StartChooser

class DetailViewModelTest {

    private val tracker: Tracker = relaxedMockk()
    private val startChooser: StartChooser = relaxedMockk()
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        viewModel = DetailViewModel(
            tracker,
            startChooser
        )
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() {
        val audio = buildAudio()

        viewModel.onViewScreen(audio)

        verifyOnce {
            tracker.trackEvent(
                withArg { event ->
                    event shouldBeInstanceOf DetailTrackerEvent.ViewScreen::class.java
                    val parameters = event.parameters
                    parameters[TRACKER_KEY_AUDIO_ID] shouldBe audio.id
                    parameters[TRACKER_KEY_AUDIO_TITLE] shouldBe audio.title
                }
            )
        }
    }

    @Test
    fun `WHEN onBackClicked THEN track event`() {
        val audio = buildAudio()

        viewModel.onBackClicked(audio)

        verifyOnce {
            tracker.trackEvent(
                withArg { event ->
                    event shouldBeInstanceOf DetailTrackerEvent.ClickBack::class.java
                    val parameters = event.parameters
                    parameters[TRACKER_KEY_AUDIO_ID] shouldBe audio.id
                    parameters[TRACKER_KEY_AUDIO_TITLE] shouldBe audio.title
                }
            )
        }
    }

    @Test
    fun `WHEN onPlayPauseClicked on play THEN track the corresponding event`() {
        val audio = buildAudio()
        val playPause = PlayPause.PLAY

        viewModel.onPlayPauseClicked(audio, playPause)

        val parameters = audio.toMap()
        verifyOnce { tracker.trackEvent(DetailTrackerEvent.ClickPlay(parameters)) }
    }

    @Test
    fun `WHEN onPlayPauseClicked on pause THEN track the corresponding event`() {
        val audio = buildAudio()
        val playPause = PlayPause.PAUSE

        viewModel.onPlayPauseClicked(audio, playPause)

        val parameters = audio.toMap()
        verifyOnce { tracker.trackEvent(DetailTrackerEvent.ClickPause(parameters)) }
    }

    @Test
    fun `WHEN onShareClicked THEN track the corresponding event`() {
        val context: Context = relaxedMockk()
        val audio = buildAudio()

        viewModel.onShareClicked(context, audio)

        verifyOnce {
            tracker.trackEvent(DetailTrackerEvent.ClickShare(audio.toMap()))
            startChooser(context, R.string.share_podcast_content, audio.title, audio.url)
        }
    }
}
