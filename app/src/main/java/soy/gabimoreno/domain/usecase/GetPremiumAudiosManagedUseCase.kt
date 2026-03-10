package soy.gabimoreno.domain.usecase

import android.content.Context
import androidx.paging.PagingData
import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class GetPremiumAudiosManagedUseCase
    @Inject
    constructor(
        private val context: Context,
        private val premiumAudiosRepository: PremiumAudiosRepository,
    ) {
        suspend operator fun invoke(
            categories: List<Category>,
        ): Either<Throwable, Flow<PagingData<PremiumAudio>>> {
            val email = context.getEmail().first()
            return premiumAudiosRepository.getPremiumAudioMediator(categories, email)
        }
    }
