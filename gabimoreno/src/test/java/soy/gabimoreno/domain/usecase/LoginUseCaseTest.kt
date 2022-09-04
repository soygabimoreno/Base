package soy.gabimoreno.domain.usecase

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.network.repository.NetworkGabiMorenoRepository
import soy.gabimoreno.domain.model.login.AuthCookie

@ExperimentalCoroutinesApi
class LoginUseCaseTest {

    private val repository: NetworkGabiMorenoRepository = mockk()
    private lateinit var useCase: LoginUseCase

    @Before
    fun setUp() {
        useCase = LoginUseCase(
            repository
        )
    }

    @Test
    fun `GIVEN a success authCookie WHEN invoke THEN get the expected result`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookie: AuthCookie = relaxedMockk()
            coEvery { repository.generateAuthCookie(email, password) } returns authCookie.right()

            val result = useCase(email, password)

            result.isRight().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
        }

    @Test
    fun `GIVEN a failure authCookie WHEN invoke THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val throwable = Throwable()
            coEvery { repository.generateAuthCookie(email, password) } returns throwable.left()

            val result = useCase(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
        }
}
