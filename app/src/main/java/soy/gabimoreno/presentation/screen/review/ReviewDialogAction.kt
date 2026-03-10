package soy.gabimoreno.presentation.screen.review

import android.app.Activity

sealed interface ReviewDialogAction {
    data object OnShouldntWeAskAgainChanged : ReviewDialogAction
    data object OnDismissDialog : ReviewDialogAction
    data object OnConfirmDialog : ReviewDialogAction
    data object OnEmailSent : ReviewDialogAction
    data class SetOnReviewDialog(
        val activity: Activity,
    ) : ReviewDialogAction
}
