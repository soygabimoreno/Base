@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.courses.detail

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_AUDIO_COURSE_ID
import soy.gabimoreno.data.tracker.main.AudioCoursesDetailTrackerEvent
import soy.gabimoreno.domain.usecase.GetAudioCourseByIdUseCase
import soy.gabimoreno.domain.usecase.MarkAudioCourseItemAsListenedUseCase
import soy.gabimoreno.domain.usecase.UpdateAudioItemFavoriteStateUseCase
import soy.gabimoreno.fake.buildAudioCourse

class AudioCourseDetailViewModelTest {
    private val getAudioCourseByIdUseCase = relaxedMockk<GetAudioCourseByIdUseCase>()
    private val markAudioCourseItemAsListenedUseCase =
        relaxedMockk<MarkAudioCourseItemAsListenedUseCase>()
    private val updateAudioItemFavoriteStateUseCase =
        relaxedMockk<UpdateAudioItemFavoriteStateUseCase>()
    private val tracker: Tracker = relaxedMockk()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AudioCourseDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel =
            AudioCourseDetailViewModel(
                getAudioCourseByIdUseCase,
                markAudioCourseItemAsListenedUseCase,
                updateAudioItemFavoriteStateUseCase,
                tracker,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() =
        runTest {
            val audioCourseId = "audioCourseId"
            viewModel.onViewScreen(audioCourseId)
            advanceUntilIdle()

            verifyOnce {
                tracker.trackEvent(
                    AudioCoursesDetailTrackerEvent.ViewScreen(
                        parameters = mapOf(TRACKER_KEY_AUDIO_COURSE_ID to audioCourseId),
                    ),
                )
            }
        }

    @Test
    fun `WHEN OnShowAudioCourseOnWebClicked THEN track event and emit ShowAudioCourseOnWeb event`() =
        runTest {
            val audioCourse = buildAudioCourse()
            val action = AudioCourseDetailAction.OpenAudioCourseOnWeb(audioCourseId = audioCourse.id)
            val eventChannel = Channel<AudioCourseDetailEvent>(Channel.UNLIMITED)
            val job =
                launch {
                    viewModel.events.collect {
                        eventChannel.trySend(it)
                    }
                }

            viewModel.onAction(action)
            advanceUntilIdle()

            eventChannel.receive() shouldBeEqualTo AudioCourseDetailEvent.OpenAudioCourseOnWeb
            verifyOnce {
                tracker.trackEvent(
                    AudioCoursesDetailTrackerEvent.ViewOnWebScreen(
                        parameters = mapOf(TRACKER_KEY_AUDIO_COURSE_ID to audioCourse.id),
                    ),
                )
            }
            job.cancel()
        }

    @Test
    fun `GIVEN success WHEN onViewScreen THEN state is updated`() =
        runTest {
            val audioCourse = buildAudioCourse()
            coEvery { getAudioCourseByIdUseCase(audioCourse.id) } returns flowOf(audioCourse).right()

            viewModel.onViewScreen(audioCourse.id)
            advanceUntilIdle()

            viewModel.state.isLoading shouldBeEqualTo false
            viewModel.state.audioCourse shouldBeEqualTo audioCourse
        }

    @Test
    fun `GIVEN failure WHEN onViewScreen THEN emits error event`() =
        runTest {
            val audioCourse = buildAudioCourse()
            val exception = Throwable("Something went wrong")
            coEvery { getAudioCourseByIdUseCase(audioCourse.id) } returns exception.left()
            val eventChannel = Channel<AudioCourseDetailEvent>(Channel.UNLIMITED)
            val job =
                launch {
                    viewModel.events.collect {
                        eventChannel.trySend(it)
                    }
                }

            viewModel.onViewScreen(audioCourse.id)
            advanceUntilIdle()

            viewModel.state.isLoading shouldBeEqualTo false
            viewModel.state.audioCourse shouldBeEqualTo null
            eventChannel.receive() shouldBeEqualTo AudioCourseDetailEvent.Error(exception)
            job.cancel()
        }
}
