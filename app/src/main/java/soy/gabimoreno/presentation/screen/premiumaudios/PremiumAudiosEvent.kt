package soy.gabimoreno.presentation.screen.premiumaudios

sealed interface PremiumAudiosEvent {
    data class Error(
        val error: Throwable?,
    ) : PremiumAudiosEvent

    data object ShowTokenExpiredError : PremiumAudiosEvent
    data class ShowDetail(
        val premiumAudioId: String,
    ) : PremiumAudiosEvent
}
