@file:OptIn(ExperimentalCoroutinesApi::class)

package soy.gabimoreno.presentation.screen.review

import io.mockk.coEvery
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.usecase.GetInAppReviewCounterUseCase
import soy.gabimoreno.domain.usecase.SetShouldIShowInAppReviewUseCase
import soy.gabimoreno.presentation.screen.review.manager.InAppReviewEvent
import soy.gabimoreno.presentation.screen.review.manager.InAppReviewManager

class ReviewDialogViewModelTest {
    private val inAppReviewManager = relaxedMockk<InAppReviewManager>()
    private val getInAppReviewCounterUseCase = relaxedMockk<GetInAppReviewCounterUseCase>()
    private val setShouldIShowInAppReviewUseCase = relaxedMockk<SetShouldIShowInAppReviewUseCase>()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ReviewDialogViewModel

    @Before
    fun setUp() {
        coEvery { setShouldIShowInAppReviewUseCase(any()) } just runs
        coEvery {
            inAppReviewManager.inAppPreviewEvents
        } returns MutableSharedFlow<InAppReviewEvent>().asSharedFlow()
        coEvery { getInAppReviewCounterUseCase() } returns emptyFlow()
        Dispatchers.setMain(dispatcher)

        viewModel =
            ReviewDialogViewModel(
                inAppReviewManager,
                getInAppReviewCounterUseCase,
                setShouldIShowInAppReviewUseCase,
                dispatcher,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN default state WHEN initialized THEN shouldNotShowDialog`() {
        viewModel.state.shouldIShowReviewDialog shouldBe false
        viewModel.state.reviewDialogStep shouldBe ReviewDialogStep.AskIfUserLikesTheApp
    }

    @Test
    fun `GIVEN state WHEN OnShouldntWeAskAgainChanged THEN stateToggles`() {
        viewModel.onAction(ReviewDialogAction.OnShouldntWeAskAgainChanged)
        viewModel.state.shouldntWeAskAgain shouldBe true

        viewModel.onAction(ReviewDialogAction.OnShouldntWeAskAgainChanged)

        viewModel.state.shouldntWeAskAgain shouldBe false
    }

    @Test
    fun `GIVEN counter is 3 WHEN init THEN should show review dialog`() =
        runTest {
            coEvery { getInAppReviewCounterUseCase() } returns flowOf(3)
            val viewModel =
                ReviewDialogViewModel(
                    inAppReviewManager = inAppReviewManager,
                    getInAppReviewCounterUseCase = getInAppReviewCounterUseCase,
                    setShouldIShowInAppReviewUseCase = setShouldIShowInAppReviewUseCase,
                    dispatcher = dispatcher,
                )

            advanceUntilIdle()

            viewModel.state.shouldIShowReviewDialog shouldBe true
            viewModel.state.reviewDialogStep shouldBe ReviewDialogStep.AskIfUserLikesTheApp
        }

    @Test
    fun `GIVEN counter is less than 3 WHEN init THEN should not show review dialog`() =
        runTest {
            coEvery { getInAppReviewCounterUseCase() } returns flowOf(2)
            val viewModel =
                ReviewDialogViewModel(
                    inAppReviewManager = inAppReviewManager,
                    getInAppReviewCounterUseCase = getInAppReviewCounterUseCase,
                    setShouldIShowInAppReviewUseCase = setShouldIShowInAppReviewUseCase,
                    dispatcher = dispatcher,
                )

            advanceUntilIdle()

            viewModel.state.shouldIShowReviewDialog shouldBe false
            viewModel.state.reviewDialogStep shouldBe ReviewDialogStep.AskIfUserLikesTheApp
        }
}
