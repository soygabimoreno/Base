package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import javax.inject.Inject

class GetPremiumAudiosUseCase @Inject constructor(
    private val premiumAudiosRepository: PremiumAudiosRepository,
) {

    suspend operator fun invoke(categories: List<Category>): Either<Throwable, List<PremiumAudio>> {
        return premiumAudiosRepository.getPremiumAudios(categories)
    }
}
