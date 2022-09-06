package soy.gabimoreno.data.network.repository

import arrow.core.Either
import soy.gabimoreno.data.network.mapper.toDomain
import soy.gabimoreno.data.network.service.LoginService
import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.model.login.JwtAuth
import soy.gabimoreno.domain.model.login.Member
import soy.gabimoreno.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkLoginRepository @Inject constructor(
    private val service: LoginService
) : LoginRepository {

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
        email: String
    ): Either<Throwable, Member> {
        return Either.catch {
            service.getMembers(email = email).toDomain()
        }
    }
}
