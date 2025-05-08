package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.framework.datastore.getLastPremiumAudiosFromRemoteRequestTimeMillis
import javax.inject.Inject

class GetLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase @Inject constructor(
    private val context: Context,
) {
    operator fun invoke(): Flow<Long> =
        context.getLastPremiumAudiosFromRemoteRequestTimeMillis()
}
