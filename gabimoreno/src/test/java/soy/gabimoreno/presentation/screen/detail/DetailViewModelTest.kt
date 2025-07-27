@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.detail

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.R
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyNever
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_AUDIO_ID
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_AUDIO_TITLE
import soy.gabimoreno.data.tracker.main.DetailTrackerEvent
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.domain.usecase.UpdateAudioItemFavoriteStateUseCase
import soy.gabimoreno.fake.buildAudio
import soy.gabimoreno.fake.buildPremiumAudio
import soy.gabimoreno.framework.intent.StartChooser

class DetailViewModelTest {
    private val tracker: Tracker = relaxedMockk()
    private val startChooser: StartChooser = relaxedMockk()
    private val updateAudioItemFavoriteStateUseCase =
        relaxedMockk<UpdateAudioItemFavoriteStateUseCase>()
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        viewModel =
            DetailViewModel(
                tracker,
                startChooser,
                updateAudioItemFavoriteStateUseCase,
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
                },
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
                },
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

    @Test
    fun `GIVEN PremiumAudio not marked as favorite WHEN onFavoriteStatusChanged THEN state is updated and favorite event is tracked`() =
        runTest {
            val audio = buildPremiumAudio(markedAsFavorite = false)
            viewModel.audioState = audio

            viewModel.onFavoriteStatusChanged()
            advanceUntilIdle()

            coVerifyOnce {
                updateAudioItemFavoriteStateUseCase(audio.id, true)
                tracker.trackEvent(DetailTrackerEvent.AddAudioToFavorite(audio.toMap()))
            }
        }

    @Test
    fun `GIVEN PremiumAudio marked as favorite WHEN onFavoriteStatusChanged THEN state is updated and unfavorite event is tracked`() =
        runTest {
            val audio = buildPremiumAudio(markedAsFavorite = true)
            viewModel.audioState = audio

            viewModel.onFavoriteStatusChanged()
            advanceUntilIdle()
            val expected = audio.copy(markedAsFavorite = false)

            coVerifyOnce {
                updateAudioItemFavoriteStateUseCase(expected.id, false)
                tracker.trackEvent(DetailTrackerEvent.RemoveAudioFromFavorite(audio.toMap()))
            }
        }

    @Test
    fun `GIVEN audioState is null WHEN onFavoriteStatusChanged THEN nothing happens`() =
        runTest {
            viewModel.audioState = null

            viewModel.onFavoriteStatusChanged()
            advanceUntilIdle()

            coVerifyNever { updateAudioItemFavoriteStateUseCase(any(), any()) }
            verifyNever { tracker.trackEvent(any()) }
        }

    @Test
    fun `GIVEN audioState is not PremiumAudio WHEN onFavoriteStatusChanged THEN nothing happens`() =
        runTest {
            viewModel.audioState = buildAudio()

            viewModel.onFavoriteStatusChanged()
            advanceUntilIdle()

            coVerifyNever { updateAudioItemFavoriteStateUseCase(any(), any()) }
            verifyNever { tracker.trackEvent(any()) }
        }
}
