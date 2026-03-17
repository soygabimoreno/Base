@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.survey

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldHaveSize
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.usecase.GetActiveSurveyUseCase
import soy.gabimoreno.domain.usecase.SetLastSurveyIdUseCase
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildSurvey

class SurveyDialogViewModelTest {
    private val getActiveSurveyUseCase = mockk<GetActiveSurveyUseCase>()
    private val setLastSurveyIdUseCase = mockk<SetLastSurveyIdUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SurveyDialogViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN survey WHEN init THEN state contains survey`() =
        runTest {
            val survey = buildSurvey()
            coEvery { getActiveSurveyUseCase() } returns right(survey)

            viewModel = SurveyDialogViewModel(getActiveSurveyUseCase, setLastSurveyIdUseCase, testDispatcher)
            advanceUntilIdle()

            viewModel.state.survey shouldBeEqualTo survey
            coVerifyOnce {
                getActiveSurveyUseCase()
            }
            coVerifyNever {
                setLastSurveyIdUseCase(any())
            }
        }

    @Test
    fun `GIVEN null survey WHEN init THEN state contains null`() =
        runTest {
            coEvery { getActiveSurveyUseCase() } returns right(null)

            viewModel = SurveyDialogViewModel(getActiveSurveyUseCase, setLastSurveyIdUseCase, testDispatcher)
            advanceUntilIdle()

            viewModel.state.survey shouldBeEqualTo null
            coVerifyOnce {
                getActiveSurveyUseCase()
            }
            coVerifyNever {
                setLastSurveyIdUseCase(any())
            }
        }

    @Test
    fun `GIVEN survey present WHEN OnConfirmDialog THEN callback is called and state is cleared`() =
        runTest {
            val survey = buildSurvey()
            coEvery { getActiveSurveyUseCase() } returns right(survey)
            coJustRun { setLastSurveyIdUseCase(survey.id) }
            viewModel = SurveyDialogViewModel(getActiveSurveyUseCase, setLastSurveyIdUseCase, testDispatcher)
            val events = mutableListOf<SurveyDialogEvent>()
            val eventJob = launch { viewModel.events.toList(events) }
            advanceUntilIdle()

            viewModel.onAction(SurveyDialogAction.OnConfirmDialog)
            advanceUntilIdle()

            events shouldHaveSize 1
            events.first() shouldBeEqualTo SurveyDialogEvent.LaunchSurvey(survey.url)
            viewModel.state.survey shouldBeEqualTo null
            coVerifyOnce {
                getActiveSurveyUseCase()
                setLastSurveyIdUseCase(survey.id)
            }
            eventJob.cancel()
        }

    @Test
    fun `GIVEN survey WHEN OnDismissDialog THEN state is cleared`() =
        runTest {
            val survey = buildSurvey()
            coEvery { getActiveSurveyUseCase() } returns right(survey)
            viewModel = SurveyDialogViewModel(getActiveSurveyUseCase, setLastSurveyIdUseCase, testDispatcher)
            advanceUntilIdle()

            viewModel.onAction(SurveyDialogAction.OnDismissDialog)
            advanceUntilIdle()

            viewModel.state.survey shouldBeEqualTo null
            coVerifyOnce {
                getActiveSurveyUseCase()
            }
            coVerifyNever {
                setLastSurveyIdUseCase(any())
            }
        }
}
