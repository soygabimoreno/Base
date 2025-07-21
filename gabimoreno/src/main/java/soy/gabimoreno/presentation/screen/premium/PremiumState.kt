package soy.gabimoreno.presentation.screen.premium

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import soy.gabimoreno.domain.model.content.PremiumAudio

data class PremiumState(
    val isLoading: Boolean = true,
    val selectedPremiumAudio: PremiumAudio? = null,
    val premiumAudioFlow: Flow<PagingData<PremiumAudio>> =
        flow { PagingData.empty<PremiumAudio>() },
    val premiumAudios: List<PremiumAudio> = EMPTY_PREMIUM_AUDIOS,
    val shouldIAccessPremium: Boolean = false,
    val hasRefreshTokenBeenCalled: Boolean = false,
)

val EMPTY_PREMIUM_AUDIOS = emptyList<PremiumAudio>()
