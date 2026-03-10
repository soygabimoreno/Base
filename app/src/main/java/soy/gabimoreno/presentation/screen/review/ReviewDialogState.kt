package soy.gabimoreno.presentation.screen.review

data class ReviewDialogState(
    val shouldIShowReviewDialog: Boolean = false,
    val shouldntWeAskAgain: Boolean = false,
    val reviewDialogStep: ReviewDialogStep = ReviewDialogStep.AskIfUserLikesTheApp,
)
