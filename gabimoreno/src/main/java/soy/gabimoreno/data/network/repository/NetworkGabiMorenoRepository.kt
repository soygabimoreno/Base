package soy.gabimoreno.data.network.repository

import androidx.annotation.VisibleForTesting
import arrow.core.Either
import soy.gabimoreno.data.network.mapper.toDomain
import soy.gabimoreno.data.network.service.GabiMorenoService
import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.model.login.JwtAuth
import soy.gabimoreno.domain.model.login.Member
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
            service.generateAuthCookie(email, password).toDomain()
        }
    }

    override suspend fun obtainToken(
        username: String,
        password: String
    ): Either<Throwable, JwtAuth> {
        return Either.catch {
            service.obtainToken(username, password).toDomain()
        }
    }

    override suspend fun getMember(
        email: String,
        token: String
    ): Either<Throwable, Member> {
        return Either.catch {
            service.getMembers(
                email = email,
                bearerToken = "$BEARER_PREFIX$token"
            ).toDomain()
        }
    }
}

@VisibleForTesting
internal const val BEARER_PREFIX = "Bearer "
