package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.data.remote.client.APIClient
import soy.gabimoreno.framework.datastore.EMPTY_BEARER_TOKEN
import soy.gabimoreno.framework.datastore.setBearerToken
import javax.inject.Inject

class ResetJwtAuthTokenUseCase @Inject constructor(
    private val context: Context,
) {

    suspend operator fun invoke() {
        APIClient.bearerToken = EMPTY_BEARER_TOKEN
        context.setBearerToken(EMPTY_BEARER_TOKEN)
    }
}
