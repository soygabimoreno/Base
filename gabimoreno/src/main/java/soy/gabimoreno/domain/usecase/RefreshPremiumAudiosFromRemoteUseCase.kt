package soy.gabimoreno.domain.usecase

import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RefreshPremiumAudiosFromRemoteUseCase @Inject constructor(
    private val saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase: SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
    private val getLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase: GetLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
) {

    suspend operator fun invoke(
        currentTimeInMillis: Long,
        timeToRefreshInMillis: Long,
    ): Boolean {
        val lastTimeInMillis =
            getLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase().first()
        saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(currentTimeInMillis)
        return currentTimeInMillis >= lastTimeInMillis + timeToRefreshInMillis
    }
}
