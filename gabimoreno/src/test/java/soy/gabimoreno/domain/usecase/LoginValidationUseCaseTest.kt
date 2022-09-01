package soy.gabimoreno.domain.usecase

import arrow.core.left
import arrow.core.right
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class LoginValidationUseCaseTest {

    private lateinit var useCase: LoginValidationUseCase

    @Before
    fun setUp() {
        useCase = LoginValidationUseCase()
    }

    @Test
    fun `GIVEN a valid email and a valid password WHEN invoke THEN return success`() {
        val email = "example@example.com"
        val password = "1234"

        val result = useCase(email, password)

        result shouldBeEqualTo Unit.right()
    }

    @Test
    fun `GIVEN a invalid email and a valid password WHEN invoke THEN return the error`() {
        val emails = listOf("example", "")
        val password = "1234"
        emails.forEach { email ->
            val result = useCase(email, password)

            result shouldBeEqualTo LoginValidationUseCase.Error.InvalidEmailFormat.left()
        }
    }

    @Test
    fun `GIVEN a valid email and an invalid password WHEN invoke THEN return the error`() {
        val email = "example@example.com"
        val passwords = listOf("", "123")
        passwords.forEach { password ->
            val result = useCase(email, password)

            result shouldBeEqualTo LoginValidationUseCase.Error.InvalidPassword.left()
        }
    }
}
