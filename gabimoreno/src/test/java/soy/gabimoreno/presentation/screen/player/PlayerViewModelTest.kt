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
import soy.gabimoreno.domain.model.audio.Audio
import soy.gabimoreno.fake.buildAudio
import soy.gabimoreno.fake.buildAudios
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
        val audio = buildAudio()

        viewModel.onViewScreen(audio)

        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ViewScreen(audio.toMap())) }
    }

    @Test
    fun `GIVEN the same audio and podcastIsPlaying WHEN playPauseAudio THEN play the audio and track event`() {
        val index = 0
        val audios = buildAudios()
        val currentAudio = audios[index]
        every { viewModel.currentPlayingAudio.value } returns currentAudio

        // TODO
//        viewModel.playPauseAudio(audios, currentAudio)

//        verifyOnce { mediaPlayerServiceConnection.playPodcast(audios) }
    }

    @Test
    fun `GIVEN a new audio WHEN playPauseAudio THEN play the new audio and track event`() {
        val audios = buildAudios()
        val currentPlayingAudio = audios[0]
        val newAudio: Audio = audios[1]
        every { viewModel.currentPlayingAudio.value } returns currentPlayingAudio

        viewModel.playPauseAudio(audios, newAudio)

        verifyOnce {
            tracker.trackEvent(PlayerTrackerEvent.PlayFromMediaId(newAudio.toMap()))
            mediaPlayerServiceConnection.transportControls.playFromMediaId(newAudio.id, null)
        }
    }

    @Test
    fun `WHEN onPlayPauseClickedFromPlayer on play THEN track the corresponding event`() {
        val audio = buildAudio()
        val playPause = PlayPause.PLAY

        viewModel.onPlayPauseClickedFromPlayer(audio, playPause)

        val parameters = audio.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ClickPlayFromPlayer(parameters)) }
    }

    @Test
    fun `WHEN onPlayPauseClicked on pause THEN track the corresponding event`() {
        val audio = buildAudio()
        val playPause = PlayPause.PAUSE

        viewModel.onPlayPauseClickedFromPlayer(audio, playPause)

        val parameters = audio.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ClickPauseFromPlayer(parameters)) }
    }

    @Test
    fun `WHEN onPlayPauseClickedFromAudioBottomBar on play THEN track the corresponding event`() {
        val audio = buildAudio()
        val playPause = PlayPause.PLAY

        viewModel.onPlayPauseClickedFromAudioBottomBar(audio, playPause)

        val parameters = audio.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ClickPlayFromAudioBottomBar(parameters)) }
    }

    @Test
    fun `WHEN onPlayPauseClickedFromAudioBottomBar on pause THEN track the corresponding event`() {
        val audio = buildAudio()
        val playPause = PlayPause.PAUSE

        viewModel.onPlayPauseClickedFromAudioBottomBar(audio, playPause)

        val parameters = audio.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.ClickPauseFromAudioBottomBar(parameters)) }
    }

    @Test
    fun `WHEN togglePlaybackState on play THEN track the corresponding event`() {
        // TODO
//        viewModel.togglePlaybackState()
    }

    @Test
    fun `WHEN onAudioBottomBarSwiped THEN track the corresponding event`() {
        val currentPlayingAudio = buildAudio()
        every { viewModel.currentPlayingAudio.value } returns currentPlayingAudio

        viewModel.onAudioBottomBarSwiped()

        val parameters = currentPlayingAudio.toMap()
        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.Stop(parameters)) }
    }

    @Test
    fun `WHEN clickFastForward THEN track the corresponding event`() {
        val currentPlayingAudio = buildAudio()
        every { viewModel.currentPlayingAudio.value } returns currentPlayingAudio

        viewModel.onForwardClicked()

        val parameters = currentPlayingAudio.toMap()
        verifyOnce {
            mediaPlayerServiceConnection.fastForward()
            tracker.trackEvent(PlayerTrackerEvent.ClickForward(parameters))
        }
    }

    @Test
    fun `WHEN clickRewind THEN track the corresponding event`() {
        val currentPlayingAudio = buildAudio()
        every { viewModel.currentPlayingAudio.value } returns currentPlayingAudio

        viewModel.onRewindClicked()

        val parameters = currentPlayingAudio.toMap()
        verifyOnce {
            mediaPlayerServiceConnection.rewind()
            tracker.trackEvent(PlayerTrackerEvent.ClickRewind(parameters))
        }
    }

    @Test
    fun `WHEN onSliderChangeFinished THEN track the corresponding event`() {

        // TODO
//        val currentPlayingAudio = buildAudio()
//        every { viewModel.currentPlayingAudio.value } returns currentPlayingAudio
//
//        mockkStatic(MediaPlayerService::class)
//        every { MediaPlayerService.currentDuration } returns 0L
//        val localSliderValue = 0f
//
//        viewModel.onSliderChangeFinished(localSliderValue)
//
//        val parameters = currentPlayingAudio.toMap() + mapOf(
//            AUDIO_PLAYBACK_POSITION to "0"
//        )
//        verifyOnce { tracker.trackEvent(PlayerTrackerEvent.SeekTo(parameters)) }
    }
}
