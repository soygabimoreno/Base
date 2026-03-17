@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.audiocourses.list

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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
import soy.gabimoreno.core.testing.verifyOnce
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.AudioCoursesListTrackerEvent
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.usecase.GetAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.RefreshBearerTokenUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.fake.buildAudioCourses

class AudioCoursesViewModelTest {
    private val getAudioCoursesUseCase = relaxedMockk<GetAudioCoursesUseCase>()
    private val getShouldIReloadAudioCoursesUseCase =
        relaxedMockk<GetShouldIReloadAudioCoursesUseCase>()
    private val setShouldIReloadAudioCoursesUseCase =
        relaxedMockk<SetShouldIReloadAudioCoursesUseCase>()
    private val refreshBearerTokenUseCase = relaxedMockk<RefreshBearerTokenUseCase>()
    private val tracker: Tracker = relaxedMockk()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AudioCoursesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getShouldIReloadAudioCoursesUseCase() } returns flowOf(false)
        viewModel =
            AudioCoursesViewModel(
                getAudioCoursesUseCase,
                getShouldIReloadAudioCoursesUseCase,
                setShouldIReloadAudioCoursesUseCase,
                refreshBearerTokenUseCase,
                tracker,
                testDispatcher,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onViewScreen THEN track event`() =
        runTest {
            val job =
                launch {
                    viewModel.state.collect { }
                }
            advanceUntilIdle()

            verifyOnce {
                tracker.trackEvent(AudioCoursesListTrackerEvent.ViewScreen)
            }
            job.cancel()
        }

    @Test
    fun `GIVEN valid courses WHEN onViewScreen THEN state contains courses and isLoading is false`() =
        runTest {
            val expectedCourses = buildAudioCourses()
            coEvery { getAudioCoursesUseCase(any()) } returns expectedCourses.right()

            val states = mutableListOf<AudioCoursesState>()
            val stateJob = launch { viewModel.state.toList(states) }
            advanceUntilIdle()

            states.last().audioCourses shouldBe expectedCourses
            states.last().isLoading shouldBe false
            stateJob.cancel()
        }

    @Test
    fun `GIVEN token expired WHEN onScreenView THEN callback is called`() =
        runTest {
            coEvery { getAudioCoursesUseCase(any()) } returns TokenExpiredException().left()
            coEvery { refreshBearerTokenUseCase() } returns TokenExpiredException().left()

            val events = mutableListOf<AudioCoursesEvent>()
            val eventJob = launch { viewModel.events.toList(events) }
            val stateJob = launch { viewModel.state.collect {} }
            advanceUntilIdle()

            events.first() shouldBeEqualTo AudioCoursesEvent.ShowTokenExpiredError
            eventJob.cancel()
            stateJob.cancel()
        }

    @Test
    fun `GIVEN token expired THEN token refreshed and courses loaded`() =
        runTest {
            val dummyCourses = buildAudioCourses()
            val categories = listOf(Category.AUDIOCOURSES)
            coEvery {
                getAudioCoursesUseCase(categories, forceRefresh = false)
            } returns TokenExpiredException().left()
            coEvery {
                getAudioCoursesUseCase(categories, forceRefresh = true)
            } returns dummyCourses.right()
            coEvery { refreshBearerTokenUseCase() } returns Unit.right()

            val stateJob = launch { viewModel.state.collect {} }
            advanceUntilIdle()

            viewModel.state.value.audioCourses shouldBe dummyCourses
            coVerifyOnce {
                refreshBearerTokenUseCase()
            }
            coVerifyOnce {
                getAudioCoursesUseCase(categories, forceRefresh = false)
            }
            coVerifyOnce {
                getAudioCoursesUseCase(categories, forceRefresh = true)
            }
            stateJob.cancel()
        }

    @Test
    fun `GIVEN unknown exception WHEN onScreenView THEN callback is called`() =
        runTest {
            val error = RuntimeException("Unknown")
            coEvery { getAudioCoursesUseCase(any()) } returns error.left()

            val events = mutableListOf<AudioCoursesEvent>()
            val eventJob = launch { viewModel.events.toList(events) }
            val stateJob = launch { viewModel.state.collect {} }
            advanceUntilIdle()

            events.first() shouldBeEqualTo AudioCoursesEvent.Error(error)
            eventJob.cancel()
            stateJob.cancel()
        }

    @Test
    fun `GIVEN user refreshes WHEN onAction THEN refresh use case is called`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val expectedCourses = buildAudioCourses()
            coEvery { getAudioCoursesUseCase(categories) } returns expectedCourses.right()

            val states = mutableListOf<AudioCoursesState>()
            val stateJob = launch { viewModel.state.toList(states) }
            viewModel.onAction(AudioCoursesAction.OnRefreshContent)
            advanceUntilIdle()

            coVerifyOnce {
                getAudioCoursesUseCase(categories, forceRefresh = true)
            }
            states.last().isRefreshing shouldBe false
            stateJob.cancel()
        }
}
