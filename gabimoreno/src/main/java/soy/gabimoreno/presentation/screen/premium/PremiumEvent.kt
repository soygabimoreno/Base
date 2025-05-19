package soy.gabimoreno.presentation.screen.premium

sealed interface PremiumEvent {
    data class Error(val error: Throwable?) : PremiumEvent
    data object ShowTokenExpiredError : PremiumEvent
    data class ShowDetail(val premiumAudioId: String) : PremiumEvent
}
