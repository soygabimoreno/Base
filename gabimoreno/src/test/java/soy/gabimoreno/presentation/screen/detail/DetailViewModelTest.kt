package soy.gabimoreno.presentation.screen.detail

import android.content.Context
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.R
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.main.DetailTrackerEvent
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.fake.buildEpisode
import soy.gabimoreno.framework.intent.StartActionView
import soy.gabimoreno.framework.intent.StartChooser

class DetailViewModelTest {

    private val tracker: Tracker = relaxedMockk()
    private val startChooser: StartChooser = relaxedMockk()
    private val startActionView: StartActionView = relaxedMockk()
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        viewModel = DetailViewModel(
            tracker,
            startChooser,
            startActionView
        )
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() {
        val episode = buildEpisode()

        viewModel.onViewScreen(episode)

        verifyOnce { tracker.trackEvent(DetailTrackerEvent.ViewScreen(episode.toMap())) }

        // TODO: Do this with slots
//        val slot = slot<Map<String, String>>()
//        verifyOnce { tracker.trackEvent(DetailTrackerEvent.ViewScreen(capture(slot))) }
//        val captured = slot.captured
//        captured[EPISODE_ID] shouldBe episode.id
//        captured[EPISODE_TITLE] shouldBe episode.title
    }

    @Test
    fun `WHEN onPlayPauseClicked on play THEN track the corresponding event`() {
        val episode = buildEpisode()
        val playPause = PlayPause.PLAY

        viewModel.onPlayPauseClicked(episode, playPause)

        val map = episode.toMap()
        verifyOnce { tracker.trackEvent(DetailTrackerEvent.ClickPlay(map)) }
    }

    @Test
    fun `WHEN onPlayPauseClicked on pause THEN track the corresponding event`() {
        val episode = buildEpisode()
        val playPause = PlayPause.PAUSE

        viewModel.onPlayPauseClicked(episode, playPause)

        val map = episode.toMap()
        verifyOnce { tracker.trackEvent(DetailTrackerEvent.ClickPause(map)) }
    }

    @Test
    fun `WHEN onShareClicked THEN track the corresponding event`() {
        val context: Context = relaxedMockk()
        val episode = buildEpisode()

        viewModel.onShareClicked(context, episode)

        verifyOnce {
            tracker.trackEvent(DetailTrackerEvent.ClickShare(episode.toMap()))
            startChooser(context, R.string.share_podcast_content, episode.title, episode.url)
        }
    }

    @Test
    fun `WHEN onInfoClicked THEN track the corresponding event`() {
        val context: Context = relaxedMockk()
        val episode = buildEpisode()

        viewModel.onInfoClicked(context, episode)

        verifyOnce {
            tracker.trackEvent(DetailTrackerEvent.ClickInfo(episode.toMap()))
            startActionView(context, episode.url)
        }
    }
}
