package soy.gabimoreno.presentation.screen.premium

sealed interface PremiumAction {
    data class OnViewScreen(val email: String, val password: String) : PremiumAction
    data class OnEmailChanged(val email: String) : PremiumAction
    data class OnPasswordChanged(val password: String) : PremiumAction

    data object OnLoginClicked : PremiumAction
    data object OnLogoutClicked : PremiumAction
    data object OnRefreshContent : PremiumAction

    data class OnPremiumItemClicked(val premiumAudioId: String) : PremiumAction
}
