package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import javax.inject.Inject

class GetPremiumAudioByIdUseCase
    @Inject
    constructor(
        private val premiumAudiosRepository: PremiumAudiosRepository,
    ) {
        suspend operator fun invoke(idPremiumAudio: String): Either<Throwable, PremiumAudio> =
            premiumAudiosRepository.getPremiumAudioById(idPremiumAudio)
    }
