package soy.gabimoreno.data.remote.datasource.premiumaudios

import arrow.core.Either
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio

interface PremiumAudiosDataSource {
    suspend fun getPremiumAudios(categories: List<Category>): Either<Throwable, List<PremiumAudio>>
}
