package soy.gabimoreno.presentation.screen.auth

sealed interface AuthEvent {
    data class Error(val error: Throwable?) : AuthEvent
    data object ShowLoginError : AuthEvent
    data object OnLoginEvent : AuthEvent
    data object ShowTokenExpiredError : AuthEvent
}
