package soy.gabimoreno.presentation.screen.premiumaudio

sealed interface PremiumAudiosEvent {
    data class Error(
        val error: Throwable?,
    ) : PremiumAudiosEvent

    data object ShowTokenExpiredError : PremiumAudiosEvent
    data class ShowDetail(
        val premiumAudioId: String,
    ) : PremiumAudiosEvent
}
