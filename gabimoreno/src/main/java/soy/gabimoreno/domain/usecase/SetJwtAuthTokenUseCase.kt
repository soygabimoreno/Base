package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.data.network.client.APIClient
import soy.gabimoreno.framework.datastore.setBearerToken
import javax.inject.Inject

class SetJwtAuthTokenUseCase @Inject constructor(
    private val context: Context
) {

    suspend operator fun invoke(token: String) {
        val bearerToken = "Bearer $token"
        APIClient.bearerToken = bearerToken
        context.setBearerToken(bearerToken)
    }
}
