package soy.gabimoreno.domain.usecase

import androidx.paging.PagingData
import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import javax.inject.Inject

class GetPremiumAudiosManagedUseCase @Inject constructor(
    private val premiumAudiosRepository: PremiumAudiosRepository,
) {
    suspend operator fun invoke(categories: List<Category>):
            Either<Throwable, Flow<PagingData<PremiumAudio>>> {
        return premiumAudiosRepository.getPremiumAudioMediator(categories)
    }
}
