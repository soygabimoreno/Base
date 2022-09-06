package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.data.network.client.APIClient
import soy.gabimoreno.framework.datastore.EMPTY_BEARER_TOKEN
import soy.gabimoreno.framework.datastore.getBearerToken
import javax.inject.Inject

class IsBearerTokenValid @Inject constructor(
    private val context: Context
) {

    suspend operator fun invoke(): Boolean {
        if (APIClient.bearerToken != null) return true
        return context.getBearerToken().first() != EMPTY_BEARER_TOKEN
    }
}
