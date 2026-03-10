package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class SetAllPremiumAudiosAsUnlistenedUseCase
    @Inject
    constructor(
        private val context: Context,
        private val premiumAudiosRepository: PremiumAudiosRepository,
    ) {
        suspend operator fun invoke() {
            val email = context.getEmail().first()
            premiumAudiosRepository.markAllPremiumAudiosAsUnlistened(email = email)
        }
    }
