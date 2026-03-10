package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.premiumaudios.DefaultPremiumAudiosRepository
import javax.inject.Inject

class RefreshPremiumAudiosUseCase
    @Inject
    constructor(
        private val remoteContentRepository: DefaultPremiumAudiosRepository,
    ) {
        suspend operator fun invoke() = remoteContentRepository.reset()
    }
