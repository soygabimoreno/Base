package soy.gabimoreno.data.network.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.network.model.AuthCookieApiModel
import soy.gabimoreno.data.network.service.GabiMorenoService

@ExperimentalCoroutinesApi
class NetworkGabiMorenoRepositoryTest {

    private val service: GabiMorenoService = mockk()
    private lateinit var repository: NetworkGabiMorenoRepository

    @Before
    fun setUp() {
        repository = NetworkGabiMorenoRepository(
            service
        )
    }

    @Test
    fun `GIVEN a success authCookie WHEN generateAuthCookie THEN get the expected result`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookieApiModel: AuthCookieApiModel = relaxedMockk()
            coEvery { service.generateAuthCookie(email, password) } returns authCookieApiModel

            val result = repository.generateAuthCookie(email, password)

            result.isRight().shouldBeTrue()
            coVerifyOnce { service.generateAuthCookie(email, password) }
        }

    @Test
    fun `GIVEN a failure authCookie WHEN generateAuthCookie THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val throwable = Throwable()
            coEvery { service.generateAuthCookie(email, password) } throws throwable

            val result = repository.generateAuthCookie(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { service.generateAuthCookie(email, password) }
        }
}
