package soy.gabimoreno.presentation.screen.profile

data class ProfileState(
    val isLoading: Boolean = true,
    val email: String = "",
    val showResetDialog: Boolean = false,
    val selectedTypeDialog: TypeDialog? = null,
)

enum class TypeDialog {
    AUDIOCOURSES,
    PODCAST,
    PREMIUM,
}
