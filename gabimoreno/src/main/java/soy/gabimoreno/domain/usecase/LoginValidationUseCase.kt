package soy.gabimoreno.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import soy.gabimoreno.core.validator.isAValidEmail
import javax.inject.Inject

class LoginValidationUseCase @Inject constructor() {

    operator fun invoke(
        email: String,
        password: String
    ): Either<Error, Unit> {
        val isAValidEmail = email.isAValidEmail()
        val isAValidPassword = password.isAValidPassword()

        if (!isAValidEmail) return Error.InvalidEmailFormat.left()
        if (!isAValidPassword) return Error.InvalidPassword.left()
        return Unit.right()
    }

    sealed class Error {
        object InvalidEmailFormat : Error()
        object InvalidPassword : Error()
    }
}

private fun String.isAValidPassword(): Boolean = length >= 4
