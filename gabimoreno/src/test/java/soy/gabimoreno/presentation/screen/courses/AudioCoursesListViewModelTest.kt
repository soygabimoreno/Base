@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.courses

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.usecase.GetAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.RefreshAudioCoursesUseCase
import soy.gabimoreno.fake.buildAudioCourses
import soy.gabimoreno.presentation.screen.courses.list.AudioCoursesListAction
import soy.gabimoreno.presentation.screen.courses.list.AudioCoursesListEvent
import soy.gabimoreno.presentation.screen.courses.list.AudioCoursesListViewModel


class AudioCoursesListViewModelTest {

    private val getCoursesUseCase = relaxedMockk<GetAudioCoursesUseCase>()
    private val refreshAudioCoursesUseCase = relaxedMockk<RefreshAudioCoursesUseCase>()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AudioCoursesListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AudioCoursesListViewModel(
            getCoursesUseCase,
            refreshAudioCoursesUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN valid courses WHEN onViewScreen THEN state contains courses and isLoading is false`() =
        runTest {
            val expectedCourses = buildAudioCourses()
            coEvery { getCoursesUseCase(any()) } returns expectedCourses.right()

            viewModel = AudioCoursesListViewModel(
                getCoursesUseCase = getCoursesUseCase,
                refreshAudioCoursesUseCase = refreshAudioCoursesUseCase,
                dispatcher = testDispatcher
            )
            advanceUntilIdle()

            viewModel.state.audioCoursesList shouldBe expectedCourses
            viewModel.state.isLoading shouldBe false
        }


    @Test
    fun `GIVEN token expired WHEN onViewScreen THEN callback is called`() = runTest {
        coEvery { getCoursesUseCase(any()) } returns TokenExpiredException().left()
        val eventChannel = Channel<AudioCoursesListEvent>(Channel.UNLIMITED)
        val job = launch {
            viewModel.events.collect {
                eventChannel.trySend(it)
            }
        }

        viewModel = AudioCoursesListViewModel(
            getCoursesUseCase,
            refreshAudioCoursesUseCase,
            testDispatcher
        )
        advanceUntilIdle()

        eventChannel.receive() shouldBeEqualTo AudioCoursesListEvent.ShowTokenExpiredError
        job.cancel()
    }

    @Test
    fun `GIVEN unknown exception WHEN onViewScreen THEN callback is called`() = runTest {
        val error = RuntimeException("Unknown")
        coEvery { getCoursesUseCase(any()) } returns error.left()
        val eventChannel = Channel<AudioCoursesListEvent>(Channel.UNLIMITED)
        val job = launch {
            viewModel.events.collect {
                eventChannel.trySend(it)
            }
        }

        viewModel = AudioCoursesListViewModel(
            getCoursesUseCase = getCoursesUseCase,
            refreshAudioCoursesUseCase = refreshAudioCoursesUseCase,
            dispatcher = testDispatcher
        )
        advanceUntilIdle()

        eventChannel.receive() shouldBeEqualTo AudioCoursesListEvent.Error(error)
        job.cancel()
    }

    @Test
    fun `GIVEN user refreshes WHEN onAction THEN refresh use case is called`() =
        runTest {
            val categories = listOf(Category.AUDIO_COURSES)
            val expectedCourses = buildAudioCourses()
            coEvery { getCoursesUseCase(categories) } returns expectedCourses.right()

            viewModel.onAction(AudioCoursesListAction.OnRefreshContent)
            advanceUntilIdle()

            coVerifyOnce {
                refreshAudioCoursesUseCase()
            }
            viewModel.state.isRefreshing shouldBe false
        }
}
