package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import javax.inject.Inject

class MarkPremiumAudioAsListenedUseCase @Inject constructor(
    private val premiumAudiosRepository: PremiumAudiosRepository
) {
    suspend operator fun invoke(idPremiumAudio: String, hasBeenListened: Boolean) {
        premiumAudiosRepository.markPremiumAudioAsListened(idPremiumAudio, hasBeenListened)
    }
}
