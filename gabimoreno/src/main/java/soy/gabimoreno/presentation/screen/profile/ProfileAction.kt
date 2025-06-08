package soy.gabimoreno.presentation.screen.profile

sealed interface ProfileAction {
    data class OnEmailChanged(val email: String) : ProfileAction
    data object OnPlaylistClicked : ProfileAction
    data object OnToggleBottomSheet : ProfileAction
    data object OnResetAudioCoursesClicked : ProfileAction
    data object OnResetPremiumAudioClicked : ProfileAction
    data object OnDismissDialog : ProfileAction
    data object OnConfirmDialog : ProfileAction
}
