package soy.gabimoreno.presentation.screen.premiumaudios

import soy.gabimoreno.domain.model.content.PremiumAudio

sealed interface PremiumAudiosAction {
    data object OnLoginClick : PremiumAudiosAction
    data object OnRefreshContent : PremiumAudiosAction
    data class OnPremiumAudiosItemClick(
        val premiumAudioId: String,
    ) : PremiumAudiosAction

    data class OnListenedToggled(
        val premiumAudio: PremiumAudio,
    ) : PremiumAudiosAction

    data object OnPlaylistClick : PremiumAudiosAction
    data class OnAddToPlaylistClick(
        val premiumAudioId: String,
    ) : PremiumAudiosAction

    data class OnFavoriteStatusChanged(
        val premiumAudio: PremiumAudio,
    ) : PremiumAudiosAction
}
