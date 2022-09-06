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
import soy.gabimoreno.data.network.model.JwtAuthApiModel
import soy.gabimoreno.data.network.model.MemberApiModel
import soy.gabimoreno.data.network.service.LoginService

@ExperimentalCoroutinesApi
class NetworkLoginRepositoryTest {

    private val service: LoginService = mockk()
    private lateinit var repository: NetworkLoginRepository

    @Before
    fun setUp() {
        repository = NetworkLoginRepository(
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

    @Test
    fun `GIVEN a success jwtAuth WHEN obtainToken THEN get the expected result`() =
        runTest {
            val username = "username"
            val password = "1234"
            val jwtAuthApiModel: JwtAuthApiModel = relaxedMockk()
            coEvery { service.obtainToken(username, password) } returns jwtAuthApiModel

            val result = repository.obtainToken(username, password)

            result.isRight().shouldBeTrue()
            coVerifyOnce { service.obtainToken(username, password) }
        }

    @Test
    fun `GIVEN a failure jwtAuth WHEN obtainToken THEN get the expected error`() =
        runTest {
            val username = "username"
            val password = "1234"
            val throwable = Throwable()
            coEvery { service.obtainToken(username, password) } throws throwable

            val result = repository.obtainToken(username, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { service.obtainToken(username, password) }
        }

    @Test
    fun `GIVEN a success member WHEN getMember THEN get the expected result`() =
        runTest {
            val email = "email@example.com"
            val token = "1234"
            val bearerToken = "$BEARER_PREFIX$token"
            val members = buildMembers()
            coEvery { service.getMembers(email, bearerToken) } returns members

            val result = repository.getMember(email, token)

            result.isRight().shouldBeTrue()
            coVerifyOnce { service.getMembers(email, bearerToken) }
        }

    @Test
    fun `GIVEN a failure member WHEN getMember THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val token = "1234"
            val bearerToken = "$BEARER_PREFIX$token"
            val throwable = Throwable()
            coEvery { service.getMembers(email, bearerToken) } throws throwable

            val result = repository.getMember(email, token)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { service.getMembers(email, bearerToken) }
        }
}

private fun buildMembers() = listOf(MemberApiModel(isActive = true))
