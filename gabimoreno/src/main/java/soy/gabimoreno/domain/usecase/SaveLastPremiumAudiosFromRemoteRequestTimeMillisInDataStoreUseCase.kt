package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.framework.datastore.setLastPremiumAudiosFromRemoteRequestTimeMillis
import javax.inject.Inject

class SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase @Inject constructor(
    private val context: Context,
) {
    suspend operator fun invoke(timeMillis: Long) {
        context.setLastPremiumAudiosFromRemoteRequestTimeMillis(timeMillis)
    }
}
