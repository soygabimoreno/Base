package soy.gabimoreno.presentation.screen.review

sealed interface ReviewDialogEvent {
    data object ShowSendEmailComment : ReviewDialogEvent
    data object ShowInAppReviewManager : ReviewDialogEvent
}
