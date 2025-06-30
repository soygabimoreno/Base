@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.courses.list

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
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.usecase.GetAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.RefreshBearerTokenUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.fake.buildAudioCourses

class AudioCoursesListViewModelTest {

    private val getCoursesUseCase = relaxedMockk<GetAudioCoursesUseCase>()
    private val getShouldIReloadAudioCoursesUseCase =
        relaxedMockk<GetShouldIReloadAudioCoursesUseCase>()
    private val setShouldIReloadAudioCoursesUseCase =
        relaxedMockk<SetShouldIReloadAudioCoursesUseCase>()
    private val refreshBearerTokenUseCase = relaxedMockk<RefreshBearerTokenUseCase>()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AudioCoursesListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getShouldIReloadAudioCoursesUseCase() } returns flowOf(false)
        viewModel = AudioCoursesListViewModel(
            getCoursesUseCase,
            getShouldIReloadAudioCoursesUseCase,
            setShouldIReloadAudioCoursesUseCase,
            refreshBearerTokenUseCase,
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

            val states = mutableListOf<AudioCoursesListState>()
            val stateJob = launch { viewModel.state.toList(states) }
            advanceUntilIdle()

            states.last().audiocourses shouldBe expectedCourses
            states.last().isLoading shouldBe false
            stateJob.cancel()
        }

    @Test
    fun `GIVEN token expired WHEN onScreenView THEN callback is called`() = runTest {
        coEvery { getCoursesUseCase(any()) } returns TokenExpiredException().left()
        coEvery { refreshBearerTokenUseCase() } returns TokenExpiredException().left()

        val events = mutableListOf<AudioCoursesListEvent>()
        val eventJob = launch { viewModel.events.toList(events) }
        val stateJob = launch { viewModel.state.collect {} }
        advanceUntilIdle()

        events.first() shouldBeEqualTo AudioCoursesListEvent.ShowTokenExpiredError
        eventJob.cancel()
        stateJob.cancel()
    }

    @Test
    fun `GIVEN token expired THEN token refreshed and courses loaded`() = runTest {
        val dummyCourses = buildAudioCourses()
        val categories = listOf(Category.AUDIOCOURSES)
        coEvery {
            getCoursesUseCase(categories, forceRefresh = false)
        } returns TokenExpiredException().left()
        coEvery {
            getCoursesUseCase(categories, forceRefresh = true)
        } returns dummyCourses.right()
        coEvery { refreshBearerTokenUseCase() } returns Unit.right()

        val stateJob = launch { viewModel.state.collect {} }
        advanceUntilIdle()

        viewModel.state.value.audiocourses shouldBe dummyCourses
        coVerifyOnce {
            refreshBearerTokenUseCase()
        }
        coVerifyOnce {
            getCoursesUseCase(categories, forceRefresh = false)
        }
        coVerifyOnce {
            getCoursesUseCase(categories, forceRefresh = true)
        }
        stateJob.cancel()
    }

    @Test
    fun `GIVEN unknown exception WHEN onScreenView THEN callback is called`() = runTest {
        val error = RuntimeException("Unknown")
        coEvery { getCoursesUseCase(any()) } returns error.left()

        val events = mutableListOf<AudioCoursesListEvent>()
        val eventJob = launch { viewModel.events.toList(events) }
        val stateJob = launch { viewModel.state.collect {} }
        advanceUntilIdle()

        events.first() shouldBeEqualTo AudioCoursesListEvent.Error(error)
        eventJob.cancel()
        stateJob.cancel()
    }

    @Test
    fun `GIVEN user refreshes WHEN onAction THEN refresh use case is called`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val expectedCourses = buildAudioCourses()
            coEvery { getCoursesUseCase(categories) } returns expectedCourses.right()

            val states = mutableListOf<AudioCoursesListState>()
            val stateJob = launch { viewModel.state.toList(states) }
            viewModel.onAction(AudioCoursesListAction.OnRefreshContent)
            advanceUntilIdle()

            coVerifyOnce {
                getCoursesUseCase(categories, forceRefresh = true)
            }
            states.last().isRefreshing shouldBe false
            stateJob.cancel()
        }
}
