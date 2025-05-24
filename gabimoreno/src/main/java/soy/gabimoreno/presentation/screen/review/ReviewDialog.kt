package soy.gabimoreno.presentation.screen.review

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import soy.gabimoreno.R
import soy.gabimoreno.presentation.ui.dialog.CustomDialog
import soy.gabimoreno.presentation.ui.dialog.TypeDialog
import soy.gabimoreno.util.sendEmail


@Composable
fun ReviewDialog(
    viewModel: ReviewDialogViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val reviewStep = state.reviewDialogStep
    val stringTo = stringResource(R.string.review_feedback_email_destination)
    val stringSubject = stringResource(R.string.review_feedback_email_subject)
    val stringBody = stringResource(R.string.review_feedback_email_body)
    val stringError = stringResource(R.string.review_feedback_email_error)

    LaunchedEffect(Unit) {
        launch {
            viewModel.events.collect { event ->
                when (event) {
                    ReviewDialogEvent.ShowSendEmailComment -> {
                        context.sendEmail(
                            to = stringTo,
                            subject = stringSubject,
                            body = stringBody,
                            errorText = stringError
                        )
                        viewModel.onAction(ReviewDialogAction.OnEmailSent)
                    }

                    ReviewDialogEvent.ShowInAppReviewManager -> {
                        val activity = context as? Activity
                        activity?.let {
                            viewModel.onAction(ReviewDialogAction.SetOnReviewDialog(activity))
                        }
                    }
                }
            }
        }
    }

    if (state.shouldIShowReviewDialog) {
        when (reviewStep) {
            ReviewDialogStep.AskIfUserLikesTheApp -> {
                CustomDialog(
                    title = stringResource(R.string.review_dialog_title),
                    text = EMPTY_STRING,
                    confirmText = stringResource(R.string.review_dialog_rate_app_yes),
                    dismissText = stringResource(R.string.review_dialog_rate_app_no),
                    onConfirm = { viewModel.onAction(ReviewDialogAction.OnConfirmDialog) },
                    onDismiss = { viewModel.onAction(ReviewDialogAction.OnDismissDialog) },
                    typeDialog = TypeDialog.CONFIRMATION
                )
            }

            ReviewDialogStep.AskIfUserWantsToLeaveComment -> {
                CustomDialog(
                    title = stringResource(R.string.review_title_email_comment),
                    text = stringResource(R.string.review_text_email_comment),
                    confirmText = stringResource(R.string.review_dialog_rate_app_yes),
                    dismissText = stringResource(R.string.review_dialog_rate_app_no),
                    isCheckboxChecked = state.shouldntWeAskAgain,
                    onCheckboxChanged = { viewModel.onAction(ReviewDialogAction.OnShouldntWeAskAgainChanged) },
                    checkBoxText = stringResource(R.string.review_dialog_dont_ask_again),
                    onConfirm = { viewModel.onAction(ReviewDialogAction.OnConfirmDialog) },
                    onDismiss = { viewModel.onAction(ReviewDialogAction.OnDismissDialog) },
                    typeDialog = TypeDialog.CONFIRMATION_ERROR
                )
            }

            ReviewDialogStep.AskIfUserWantsToRate -> {
                CustomDialog(
                    title = stringResource(R.string.review_title_do_want_to_rate_app),
                    text = stringResource(R.string.review_text_do_want_to_rate_app),
                    confirmText = stringResource(R.string.review_dialog_rate_app_yes),
                    dismissText = stringResource(R.string.review_dialog_rate_app_no),
                    isCheckboxChecked = state.shouldntWeAskAgain,
                    onCheckboxChanged = { viewModel.onAction(ReviewDialogAction.OnShouldntWeAskAgainChanged) },
                    checkBoxText = stringResource(R.string.review_dialog_dont_ask_again),
                    onConfirm = { viewModel.onAction(ReviewDialogAction.OnConfirmDialog) },
                    onDismiss = { viewModel.onAction(ReviewDialogAction.OnDismissDialog) },
                    typeDialog = TypeDialog.CONFIRMATION_WITH_CHECKBOX
                )
            }

            ReviewDialogStep.Done -> {
                CustomDialog(
                    title = stringResource(R.string.review_dialog_done),
                    text = EMPTY_STRING,
                    confirmText = stringResource(R.string.review_dialog_rate_app_yes),
                    onConfirm = { viewModel.onAction(ReviewDialogAction.OnConfirmDialog) },
                    typeDialog = TypeDialog.SUCCESS
                )
            }

            else -> Unit
        }
    }
}

private const val EMPTY_STRING = ""
