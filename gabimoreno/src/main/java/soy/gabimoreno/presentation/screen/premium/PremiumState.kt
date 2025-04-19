package soy.gabimoreno.presentation.screen.premium

import soy.gabimoreno.domain.model.content.PremiumAudio

data class PremiumState(
    val isLoading: Boolean = true,

    val email: String = EMPTY_EMAIL,
    val password: String = EMPTY_PASSWORD,
    val showInvalidEmailFormatError: Boolean = false,
    val showInvalidPasswordError: Boolean = false,

    val premiumAudios: List<PremiumAudio> = EMPTY_PREMIUM_AUDIOS,
    val shouldShowAccess: Boolean = false,
    val shouldShowPremium: Boolean = false,
)

private const val EMPTY_EMAIL = ""
private const val EMPTY_PASSWORD = ""
private val EMPTY_PREMIUM_AUDIOS = emptyList<PremiumAudio>()
