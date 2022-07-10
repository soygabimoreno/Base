package soy.gabimoreno.presentation.screen.player

import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.PlayerTrackerEvent
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.fake.buildEpisode
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
    fun `GIVEN the same currentPlayingEpisode than the one to play and podcastIsPlaying WHEN playPauseEpisode THEN play the episode and track event`() {

        // TODO
//        val index = 0
//        val episodes = buildEpisodes()
//        val currentEpisode = episodes[index]
//        every { viewModel.currentPlayingEpisode.value } returns currentEpisode
//        val podcastIsPlaying = true
//        every { viewModel.podcastIsPlaying } returns podcastIsPlaying
//
//        viewModel.playPauseEpisode(episodes, currentEpisode)
//
//        verifyOnce { mediaPlayerServiceConnection.playPodcast(episodes) }
    }
}
