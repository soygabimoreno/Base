package soy.gabimoreno.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import soy.gabimoreno.auth.validator.isAValidEmail
import soy.gabimoreno.auth.validator.isAValidPassword
import javax.inject.Inject

class LoginValidationUseCase
    @Inject
    constructor() {
        operator fun invoke(
            email: String,
            password: String,
        ): Either<Error, Unit> {
            val isAValidEmail = isAValidEmail(email)
            val isAValidPassword = isAValidPassword(password)

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
