package soy.gabimoreno.domain.repository.premiumaudios

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import soy.gabimoreno.data.local.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPremiumAudiosRepository @Inject constructor(
    private val localPremiumAudiosDataSource: LocalPremiumAudiosDataSource,
    private val remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource,
    private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
) : PremiumAudiosRepository {

    override suspend fun getPremiumAudios(
        categories: List<Category>,
    ): Either<Throwable, List<PremiumAudio>> {
        if (refreshPremiumAudiosFromRemoteUseCase(
                currentTimeInMillis = System.currentTimeMillis(),
                timeToRefreshInMillis = TWELVE_HOURS_IN_MILLIS
            ) || localPremiumAudiosDataSource.isEmpty()
        ) {
            remotePremiumAudiosDataSource.getPremiumAudios(categories)
                .fold(
                    {
                        return it.left()
                    }, { premiumAudios ->
                        localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
                    }
                )
        }
        return localPremiumAudiosDataSource.getPremiumAudios().right()
    }

    suspend fun reset() {
        localPremiumAudiosDataSource.reset()
    }
}

internal const val TWELVE_HOURS_IN_MILLIS = 12 * 60 * 60 * 1000L
