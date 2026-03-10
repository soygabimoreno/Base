package soy.gabimoreno.domain.repository.login

import arrow.core.Either
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.model.login.JwtAuth
import soy.gabimoreno.domain.model.login.Member
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteLoginRepository
    @Inject
    constructor(
        private val loginDatasource: LoginDatasource,
    ) : LoginRepository {
        override suspend fun generateAuthCookie(
            email: String,
            password: String,
        ): Either<Throwable, AuthCookie> = loginDatasource.generateAuthCookie(email, password)

        override suspend fun obtainToken(
            username: String,
            password: String,
        ): Either<Throwable, JwtAuth> = loginDatasource.obtainToken(username, password)

        override suspend fun getMember(email: String): Either<Throwable, Member> =
            loginDatasource.getMember(email = email)
    }
