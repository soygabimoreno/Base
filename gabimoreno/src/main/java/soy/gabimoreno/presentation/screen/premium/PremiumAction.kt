package soy.gabimoreno.presentation.screen.premium

import soy.gabimoreno.domain.model.content.PremiumAudio

sealed interface PremiumAction {
    data object OnLoginClicked : PremiumAction
    data object OnRefreshContent : PremiumAction
    data class OnPremiumItemClicked(val premiumAudioId: String) : PremiumAction
    data class OnListenedToggled(val premiumAudio: PremiumAudio) : PremiumAction
    data object OnPlaylistClicked : PremiumAction
}
