package soy.gabimoreno.data.network.repository

import arrow.core.Either
import soy.gabimoreno.data.network.mapper.toDomain
import soy.gabimoreno.data.network.service.GabiMorenoService
import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.repository.GabiMorenoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkGabiMorenoRepository @Inject constructor(
    private val service: GabiMorenoService
) : GabiMorenoRepository {

    override suspend fun generateAuthCookie(
        email: String,
        password: String
    ): Either<Throwable, AuthCookie> {
        return Either.catch {
            val authCookieApiModel = service.generateAuthCookie(email, password)
            authCookieApiModel.toDomain()
        }
    }
}
