package soy.gabimoreno.presentation.screen.survey

sealed interface SurveyDialogEvent {
    data class LaunchSurvey(
        val url: String,
    ) : SurveyDialogEvent
}
