package soy.gabimoreno.presentation.screen.review

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.usecase.GetInAppReviewCounterUseCase
import soy.gabimoreno.domain.usecase.SetShouldIShowInAppReviewUseCase
import soy.gabimoreno.framework.datastore.InAppReviewPrefs
import soy.gabimoreno.presentation.screen.review.manager.InAppReviewEvent
import soy.gabimoreno.presentation.screen.review.manager.InAppReviewManager
import javax.inject.Inject

@HiltViewModel
class ReviewDialogViewModel
    @Inject
    constructor(
        private val inAppReviewManager: InAppReviewManager,
        private val getInAppReviewCounterUseCase: GetInAppReviewCounterUseCase,
        private val setShouldIShowInAppReviewUseCase: SetShouldIShowInAppReviewUseCase,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        var state by mutableStateOf(ReviewDialogState())
            private set

        private val eventChannel = MutableSharedFlow<ReviewDialogEvent>()
        val events = eventChannel.asSharedFlow()

        init {
            viewModelScope.launch(dispatcher) {
                getInAppReviewCounterUseCase()
                    .collect { counter ->
                        if (counter >= SHOULD_I_SHOW_REVIEW_DIALOG) showReviewDialog()
                    }
            }
            listenToInAppReviewEvents()
        }

        private fun listenToInAppReviewEvents() {
            viewModelScope.launch(dispatcher) {
                inAppReviewManager.inAppPreviewEvents
                    .collect { event ->
                        when (event) {
                            InAppReviewEvent.Completed -> {
                                setReviewDialogStepDone()
                                setDontShowAgain()
                            }

                            InAppReviewEvent.Failed -> {
                                setReviewDialogStepDone()
                            }
                        }
                    }
            }
        }

        fun onAction(action: ReviewDialogAction) {
            when (action) {
                ReviewDialogAction.OnShouldntWeAskAgainChanged -> {
                    state = state.copy(shouldntWeAskAgain = !state.shouldntWeAskAgain)
                }

                ReviewDialogAction.OnConfirmDialog -> onDialogConfirmed()
                ReviewDialogAction.OnDismissDialog -> onDialogDismissed()
                ReviewDialogAction.OnEmailSent -> {
                    setReviewDialogStepDone()
                }

                is ReviewDialogAction.SetOnReviewDialog -> {
                    viewModelScope.launch(dispatcher) {
                        inAppReviewManager.onInAppReviewClicked(action.activity)
                    }
                }
            }
        }

        private fun onDialogConfirmed() {
            when (state.reviewDialogStep) {
                ReviewDialogStep.AskIfUserLikesTheApp -> {
                    state = state.copy(reviewDialogStep = ReviewDialogStep.AskIfUserWantsToRate)
                }

                ReviewDialogStep.AskIfUserWantsToLeaveComment -> {
                    viewModelScope.launch(dispatcher) {
                        eventChannel.emit(ReviewDialogEvent.ShowSendEmailComment)
                        setDontShowAgain()
                        state = state.copy(reviewDialogStep = ReviewDialogStep.Done)
                    }
                }

                ReviewDialogStep.AskIfUserWantsToRate -> {
                    if (state.shouldntWeAskAgain) setDontShowAgain()
                    viewModelScope.launch(dispatcher) {
                        eventChannel.emit(ReviewDialogEvent.ShowInAppReviewManager)
                    }
                }

                else -> {
                    state = state.copy(shouldIShowReviewDialog = false)
                }
            }
        }

        private fun onDialogDismissed() {
            state =
                when (state.reviewDialogStep) {
                    ReviewDialogStep.AskIfUserLikesTheApp -> {
                        state.copy(reviewDialogStep = ReviewDialogStep.AskIfUserWantsToLeaveComment)
                    }

                    else -> {
                        if (state.shouldntWeAskAgain) setDontShowAgain()
                        state.copy(reviewDialogStep = ReviewDialogStep.None)
                    }
                }
        }

        private fun setReviewDialogStepDone() {
            state = state.copy(reviewDialogStep = ReviewDialogStep.Done)
            viewModelScope.launch(dispatcher) {
                delay(DELAY_AUTOCLOSE_DONE_DIALOG)
                state = state.copy(shouldIShowReviewDialog = false)
            }
        }

        private fun showReviewDialog() {
            state =
                state.copy(
                    shouldIShowReviewDialog = true,
                    reviewDialogStep = ReviewDialogStep.AskIfUserLikesTheApp,
                )
        }

        private fun setDontShowAgain() {
            viewModelScope.launch(dispatcher) {
                setShouldIShowInAppReviewUseCase(InAppReviewPrefs.USER_DO_NOT_SHOW_AGAIN)
            }
        }
    }

private const val SHOULD_I_SHOW_REVIEW_DIALOG = 3
private const val DELAY_AUTOCLOSE_DONE_DIALOG = 2_000L
