package soy.gabimoreno.domain.repository

import arrow.core.Either
import soy.gabimoreno.domain.model.login.AuthCookie

interface GabiMorenoRepository {

    suspend fun generateAuthCookie(
        email: String,
        password: String
    ): Either<Throwable, AuthCookie>
}
