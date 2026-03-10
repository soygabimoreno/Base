package soy.gabimoreno.presentation.screen.review

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import soy.gabimoreno.R
import soy.gabimoreno.presentation.ui.dialog.CustomDialog
import soy.gabimoreno.presentation.ui.dialog.TypeDialog
import soy.gabimoreno.util.sendEmail

@Composable
fun ReviewDialog(viewModel: ReviewDialogViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state = viewModel.state
    val stringTo = stringResource(R.string.review_feedback_email_destination)
    val stringSubject = stringResource(R.string.review_feedback_email_subject)
    val stringBody = stringResource(R.string.review_feedback_email_body)
    val stringError = stringResource(R.string.review_feedback_email_error)

    CollectEvents(viewModel, context, stringTo, stringSubject, stringBody, stringError)

    if (state.shouldIShowReviewDialog) {
        RenderReviewDialog(state, viewModel)
    }
}

@Composable
private fun CollectEvents(
    viewModel: ReviewDialogViewModel,
    context: Context,
    to: String,
    subject: String,
    body: String,
    errorText: String,
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ReviewDialogEvent.ShowSendEmailComment -> {
                    context.sendEmail(to, subject, body, errorText)
                    viewModel.onAction(ReviewDialogAction.OnEmailSent)
                }

                ReviewDialogEvent.ShowInAppReviewManager -> {
                    val activity = context as? Activity
                    activity?.let {
                        viewModel.onAction(ReviewDialogAction.SetOnReviewDialog(it))
                    }
                }
            }
        }
    }
}

@Composable
private fun RenderReviewDialog(
    state: ReviewDialogState,
    viewModel: ReviewDialogViewModel,
) {
    when (state.reviewDialogStep) {
        ReviewDialogStep.AskIfUserLikesTheApp -> AskIfUserLikesDialog(viewModel)
        ReviewDialogStep.AskIfUserWantsToLeaveComment -> AskCommentDialog(state, viewModel)
        ReviewDialogStep.AskIfUserWantsToRate -> AskRatingDialog(state, viewModel)
        ReviewDialogStep.Done -> DoneDialog(viewModel)
        else -> Unit
    }
}

@Composable
private fun AskIfUserLikesDialog(viewModel: ReviewDialogViewModel) {
    CustomDialog(
        title = stringResource(R.string.review_dialog_title),
        text = EMPTY_STRING,
        confirmText = stringResource(R.string.review_dialog_rate_app_yes),
        dismissText = stringResource(R.string.review_dialog_rate_app_no),
        onConfirm = { viewModel.onAction(ReviewDialogAction.OnConfirmDialog) },
        onDismiss = { viewModel.onAction(ReviewDialogAction.OnDismissDialog) },
        typeDialog = TypeDialog.CONFIRMATION,
    )
}

@Composable
private fun AskCommentDialog(
    state: ReviewDialogState,
    viewModel: ReviewDialogViewModel,
) {
    CustomDialog(
        title = stringResource(R.string.review_title_email_comment),
        text = stringResource(R.string.review_text_email_comment),
        confirmText = stringResource(R.string.review_dialog_rate_app_yes),
        dismissText = stringResource(R.string.review_dialog_rate_app_no),
        isCheckboxChecked = state.shouldntWeAskAgain,
        onCheckboxChanged = {
            viewModel.onAction(ReviewDialogAction.OnShouldntWeAskAgainChanged)
        },
        checkBoxText = stringResource(R.string.review_dialog_dont_ask_again),
        onConfirm = { viewModel.onAction(ReviewDialogAction.OnConfirmDialog) },
        onDismiss = { viewModel.onAction(ReviewDialogAction.OnDismissDialog) },
        typeDialog = TypeDialog.CONFIRMATION_ERROR,
    )
}

@Composable
private fun AskRatingDialog(
    state: ReviewDialogState,
    viewModel: ReviewDialogViewModel,
) {
    CustomDialog(
        title = stringResource(R.string.review_title_do_want_to_rate_app),
        text = stringResource(R.string.review_text_do_want_to_rate_app),
        confirmText = stringResource(R.string.review_dialog_rate_app_yes),
        dismissText = stringResource(R.string.review_dialog_rate_app_no),
        isCheckboxChecked = state.shouldntWeAskAgain,
        onCheckboxChanged = {
            viewModel.onAction(ReviewDialogAction.OnShouldntWeAskAgainChanged)
        },
        checkBoxText = stringResource(R.string.review_dialog_dont_ask_again),
        onConfirm = { viewModel.onAction(ReviewDialogAction.OnConfirmDialog) },
        onDismiss = { viewModel.onAction(ReviewDialogAction.OnDismissDialog) },
        typeDialog = TypeDialog.CONFIRMATION_WITH_CHECKBOX,
    )
}

@Composable
private fun DoneDialog(viewModel: ReviewDialogViewModel) {
    CustomDialog(
        title = stringResource(R.string.review_dialog_done),
        text = EMPTY_STRING,
        confirmText = stringResource(R.string.review_dialog_rate_app_yes),
        onConfirm = { viewModel.onAction(ReviewDialogAction.OnConfirmDialog) },
        typeDialog = TypeDialog.SUCCESS,
    )
}

private const val EMPTY_STRING = ""
