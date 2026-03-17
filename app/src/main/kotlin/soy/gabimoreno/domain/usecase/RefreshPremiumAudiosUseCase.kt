package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.premiumaudio.DefaultPremiumAudiosRepository
import javax.inject.Inject

class RefreshPremiumAudiosUseCase
    @Inject
    constructor(
        private val remoteContentRepository: DefaultPremiumAudiosRepository,
    ) {
        suspend operator fun invoke() = remoteContentRepository.reset()
    }
