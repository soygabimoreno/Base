package soy.gabimoreno.presentation.screen.auth

data class AuthState(
    val isLoading: Boolean = true,

    val email: String = EMPTY_EMAIL,
    val password: String = EMPTY_PASSWORD,
    val showInvalidEmailFormatError: Boolean = false,
    val showInvalidPasswordError: Boolean = false,

    val shouldShowAccess: Boolean = false,
)

private const val EMPTY_EMAIL = ""
private const val EMPTY_PASSWORD = ""
