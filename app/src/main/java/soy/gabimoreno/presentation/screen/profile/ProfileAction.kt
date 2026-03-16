package soy.gabimoreno.presentation.screen.profile

sealed interface ProfileAction {
    data class OnEmailChanged(
        val email: String,
    ) : ProfileAction

    data object OnPlaylistClick : ProfileAction
    data object OnToggleBottomSheet : ProfileAction
    data object OnResetAudioCoursesClick : ProfileAction
    data object OnResetPremiumAudioClick : ProfileAction
    data object OnResetPodcastClick : ProfileAction
    data object OnDismissDialog : ProfileAction
    data object OnConfirmDialog : ProfileAction
}
