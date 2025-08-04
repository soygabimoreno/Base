package soy.gabimoreno.presentation.screen.survey

sealed interface SurveyDialogAction {
    data object OnDismissDialog : SurveyDialogAction
    data object OnConfirmDialog : SurveyDialogAction
}
