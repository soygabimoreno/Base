package soy.gabimoreno.presentation.screen.survey

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import soy.gabimoreno.R
import soy.gabimoreno.presentation.ui.dialog.CustomDialog
import soy.gabimoreno.presentation.ui.dialog.TypeDialog

@Composable
fun SurveyDialogRoot(viewModel: SurveyDialogViewModel = hiltViewModel()) {
    val uriHandler = LocalUriHandler.current
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SurveyDialogEvent.LaunchSurvey -> {
                    uriHandler.openUri(event.url)
                }
            }
        }
    }
    SurveyDialog(viewModel)
}

@Composable
fun SurveyDialog(viewModel: SurveyDialogViewModel) {
    val state = viewModel.state
    if (state.survey != null) {
        CustomDialog(
            title = stringResource(R.string.survey_title),
            text = state.survey.description,
            confirmText = stringResource(R.string.review_dialog_rate_app_yes),
            dismissText = stringResource(R.string.review_dialog_rate_app_no),
            onConfirm = { viewModel.onAction(SurveyDialogAction.OnConfirmDialog) },
            onDismiss = { viewModel.onAction(SurveyDialogAction.OnDismissDialog) },
            typeDialog = TypeDialog.CONFIRMATION,
        )
    }
}
