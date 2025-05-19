package soy.gabimoreno.presentation.screen.profile

sealed interface ProfileEvent {
    data class Error(val error: Throwable?) : ProfileEvent
    data class ResetSuccess(val type: TypeDialog) : ProfileEvent
}
