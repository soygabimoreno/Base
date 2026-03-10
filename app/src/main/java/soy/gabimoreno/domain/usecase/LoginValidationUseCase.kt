package soy.gabimoreno.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import soy.gabimoreno.core.validator.isAValidEmail
import javax.inject.Inject

class LoginValidationUseCase
    @Inject
    constructor() {
        operator fun invoke(
            email: String,
            password: String,
        ): Either<Error, Unit> {
            val isAValidEmail = email.isAValidEmail()
            val isAValidPassword = password.isAValidPassword()

            val error =
                when {
                    !isAValidEmail -> Error.InvalidEmailFormat
                    !isAValidPassword -> Error.InvalidPassword
                    else -> null
                }

            return error?.left() ?: Unit.right()
        }

        sealed class Error {
            object InvalidEmailFormat : Error()
            object InvalidPassword : Error()
        }
    }

private fun String.isAValidPassword(): Boolean = length >= MINIMAL_PASSWORD_LENGTH

private const val MINIMAL_PASSWORD_LENGTH = 4
