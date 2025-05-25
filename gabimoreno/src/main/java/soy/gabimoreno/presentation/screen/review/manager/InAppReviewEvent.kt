package soy.gabimoreno.presentation.screen.review.manager

sealed interface InAppReviewEvent {
    data object Completed : InAppReviewEvent
    data object Failed : InAppReviewEvent
}
