package soy.gabimoreno.data.remote.datasource.login

import arrow.core.Either
import soy.gabimoreno.data.remote.mapper.toDomain
import soy.gabimoreno.data.remote.service.LoginService
import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.model.login.JwtAuth
import soy.gabimoreno.domain.model.login.Member
import javax.inject.Inject

class RemoteLoginDatasource @Inject constructor(
    private val loginService: LoginService,
) : LoginDatasource {

    override suspend fun generateAuthCookie(
        email: String,
        password: String,
    ): Either<Throwable, AuthCookie> {
        return Either.catch {
            loginService.generateAuthCookie(email, password).toDomain()
        }
    }

    override suspend fun obtainToken(
        username: String,
        password: String,
    ): Either<Throwable, JwtAuth> {
        return Either.catch {
            loginService.obtainToken(username, password).toDomain()
        }
    }

    override suspend fun getMember(
        email: String,
    ): Either<Throwable, Member> {
        return Either.catch {
            loginService.getMembers(email = email).toDomain()
        }
    }
}
