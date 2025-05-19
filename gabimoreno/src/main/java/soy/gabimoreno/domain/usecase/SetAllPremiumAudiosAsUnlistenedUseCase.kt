package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import javax.inject.Inject

class SetAllPremiumAudiosAsUnlistenedUseCase @Inject constructor(
    private val premiumAudiosRepository: PremiumAudiosRepository,
) {
    suspend operator fun invoke() {
        premiumAudiosRepository.markAllPremiumAudiosAsUnlistened()
    }
}
