package soy.gabimoreno.presentation.screen.auth

sealed interface AuthAction {
    data class OnViewScreen(
        val email: String,
        val password: String,
    ) : AuthAction

    data class OnEmailChanged(
        val email: String,
    ) : AuthAction

    data class OnPasswordChanged(
        val password: String,
    ) : AuthAction

    data object OnLoginClick : AuthAction
    data object OnLogoutClick : AuthAction
}
