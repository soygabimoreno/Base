package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.repository.GabiMorenoRepository
import javax.inject.Inject

class GenerateAuthCookieUseCase @Inject constructor(
    private val repository: GabiMorenoRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Either<Throwable, AuthCookie> = repository.generateAuthCookie(email, password)
}
