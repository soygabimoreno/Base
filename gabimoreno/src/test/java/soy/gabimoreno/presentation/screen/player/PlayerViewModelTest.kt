package soy.gabimoreno.presentation.screen.player

import io.mockk.every
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.main.PlayerTrackerEvent
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.fake.buildEpisode
import soy.gabimoreno.fake.buildEpisodes
import soy.gabimoreno.player.service.MediaPlayerServiceConnection

class PlayerViewModelTest {

    private val tracker: Tracker = relaxedMockk()
    private val mediaPlayerServiceConnection: MediaPlayerServiceConnection = relaxedMockk()
    private lateinit var viewModel: PlayerViewModel

    @Before
    fun setUp() {
        viewModel = PlayerViewModel(
            mediaPlayerServiceConnection,
            tracker
        )
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() {
        val episode = buildEpisode()

        viewModel.onViewScreen(episode)

        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ViewScreen(episode.toMap())) }
    }

    @Test
    fun `GIVEN the same episode and podcastIsPlaying WHEN playPauseEpisode THEN play the episode and track event`() {
        val index = 0
        val episodes = buildEpisodes()
        val currentEpisode = episodes[index]
        every { viewModel.currentPlayingEpisode.value } returns currentEpisode

        // TODO
//        viewModel.playPauseEpisode(episodes, currentEpisode)

//        verifyOnce { mediaPlayerServiceConnection.playPodcast(episodes) }
    }

    @Test
    fun `GIVEN a new episode WHEN playPauseEpisode THEN play the new episode and track event`() {
        val episodes = buildEpisodes()
        val currentPlayingEpisode = episodes[0]
        val newEpisode = episodes[1]
        every { viewModel.currentPlayingEpisode.value } returns currentPlayingEpisode

        viewModel.playPauseEpisode(episodes, newEpisode)

        verifyOnce {
            tracker.trackEvent(PlayerTrackerEvent.PlayFromMediaId(newEpisode.toMap()))
            mediaPlayerServiceConnection.transportControls.playFromMediaId(newEpisode.id, null)
        }
    }

    @Test
    fun `WHEN onPlayPauseClickedFromPlayer on play THEN track the corresponding event`() {
        val episode = buildEpisode()
        val playPause = PlayPause.PLAY

        viewModel.onPlayPauseClickedFromPlayer(episode, playPause)

        val parameters = episode.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ClickPlayFromPlayer(parameters)) }
    }

    @Test
    fun `WHEN onPlayPauseClicked on pause THEN track the corresponding event`() {
        val episode = buildEpisode()
        val playPause = PlayPause.PAUSE

        viewModel.onPlayPauseClickedFromPlayer(episode, playPause)

        val parameters = episode.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ClickPauseFromPlayer(parameters)) }
    }

    @Test
    fun `WHEN onPlayPauseClickedFromAudioBottomBar on play THEN track the corresponding event`() {
        val episode = buildEpisode()
        val playPause = PlayPause.PLAY

        viewModel.onPlayPauseClickedFromAudioBottomBar(episode, playPause)

        val parameters = episode.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ClickPlayFromAudioBottomBar(parameters)) }
    }

    @Test
    fun `WHEN onPlayPauseClickedFromAudioBottomBar on pause THEN track the corresponding event`() {
        val episode = buildEpisode()
        val playPause = PlayPause.PAUSE

        viewModel.onPlayPauseClickedFromAudioBottomBar(episode, playPause)

        val parameters = episode.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ClickPauseFromAudioBottomBar(parameters)) }
    }

    @Test
    fun `WHEN togglePlaybackState on play THEN track the corresponding event`() {
        // TODO
//        viewModel.togglePlaybackState()
    }

    @Test
    fun `WHEN onAudioBottomBarSwiped THEN track the corresponding event`() {
        val currentPlayingEpisode = buildEpisode()
        every { viewModel.currentPlayingEpisode.value } returns currentPlayingEpisode

        viewModel.onAudioBottomBarSwiped()

        val parameters = currentPlayingEpisode.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.Stop(parameters)) }
    }

    @Test
    fun `WHEN clickFastForward THEN track the corresponding event`() {
        val currentPlayingEpisode = buildEpisode()
        every { viewModel.currentPlayingEpisode.value } returns currentPlayingEpisode

        viewModel.onForwardClicked()

        val parameters = currentPlayingEpisode.toMap()
        verifyOnce {
            mediaPlayerServiceConnection.fastForward()
            tracker.trackEvent(PlayerTrackerEvent.ClickForward(parameters))
        }
    }

    @Test
    fun `WHEN clickRewind THEN track the corresponding event`() {
        val currentPlayingEpisode = buildEpisode()
        every { viewModel.currentPlayingEpisode.value } returns currentPlayingEpisode

        viewModel.onRewindClicked()

        val parameters = currentPlayingEpisode.toMap()
        verifyOnce {
            mediaPlayerServiceConnection.rewind()
            tracker.trackEvent(PlayerTrackerEvent.ClickRewind(parameters))
        }
    }

    @Test
    fun `WHEN onSliderChangeFinished THEN track the corresponding event`() {

        // TODO
//        val currentPlayingEpisode = buildEpisode()
//        every { viewModel.currentPlayingEpisode.value } returns currentPlayingEpisode
//
//        mockkStatic(MediaPlayerService::class)
//        every { MediaPlayerService.currentDuration } returns 0L
//        val localSliderValue = 0f
//
//        viewModel.onSliderChangeFinished(localSliderValue)
//
//        val parameters = currentPlayingEpisode.toMap() + mapOf(
//            EPISODE_PLAYBACK_POSITION to "0"
//        )
//        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.SeekTo(parameters)) }
    }
}
