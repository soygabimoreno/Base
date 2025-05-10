package soy.gabimoreno.domain.repository.premiumaudios

import androidx.paging.PagingData
import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio

interface PremiumAudiosRepository {
    suspend fun getPremiumAudioMediator(
        categories: List<Category>,
    ): Either<Throwable, Flow<PagingData<PremiumAudio>>>

    suspend fun markPremiumAudioAsListened(id: String, hasBeenListened: Boolean)
    suspend fun getPremiumAudioById(idPremiumAudio: String): Either<Throwable, PremiumAudio>
}
