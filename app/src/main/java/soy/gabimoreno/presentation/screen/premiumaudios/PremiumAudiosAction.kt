package soy.gabimoreno.presentation.screen.premiumaudios

import soy.gabimoreno.domain.model.content.PremiumAudio

sealed interface PremiumAudiosAction {
    data object OnLoginClicked : PremiumAudiosAction
    data object OnRefreshContent : PremiumAudiosAction
    data class OnPremiumAudiosItemClicked(
        val premiumAudioId: String,
    ) : PremiumAudiosAction

    data class OnListenedToggled(
        val premiumAudio: PremiumAudio,
    ) : PremiumAudiosAction

    data object OnPlaylistClicked : PremiumAudiosAction
    data class OnAddToPlaylistClicked(
        val premiumAudioId: String,
    ) : PremiumAudiosAction

    data class OnFavoriteStatusChanged(
        val premiumAudio: PremiumAudio,
    ) : PremiumAudiosAction
}
