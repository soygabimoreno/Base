package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.framework.datastore.setEmail
import soy.gabimoreno.framework.datastore.setPassword
import javax.inject.Inject

class SaveCredentialsInDataStoreUseCase @Inject constructor(
    private val context: Context
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ) {
        context.setEmail(email)
        context.setPassword(password)
    }
}
