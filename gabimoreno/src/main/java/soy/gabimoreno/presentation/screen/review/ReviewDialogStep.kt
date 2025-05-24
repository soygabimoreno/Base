package soy.gabimoreno.presentation.screen.review

sealed class ReviewDialogStep {
    data object AskIfUserLikesApp : ReviewDialogStep()
    data object AskIfUserWantsToRate : ReviewDialogStep()
    data object AskIfUserWantsToLeaveComment : ReviewDialogStep()
    data object Done : ReviewDialogStep()
    data object None : ReviewDialogStep()
}
