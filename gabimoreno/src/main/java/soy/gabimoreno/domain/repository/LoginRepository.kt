package soy.gabimoreno.domain.repository

import arrow.core.Either
import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.model.login.JwtAuth
import soy.gabimoreno.domain.model.login.Member

interface LoginRepository {

    suspend fun generateAuthCookie(
        email: String,
        password: String
    ): Either<Throwable, AuthCookie>

    suspend fun obtainToken(
        username: String,
        password: String
    ): Either<Throwable, JwtAuth>

    suspend fun getMember(
        email: String,
        token: String
    ): Either<Throwable, Member>
}
