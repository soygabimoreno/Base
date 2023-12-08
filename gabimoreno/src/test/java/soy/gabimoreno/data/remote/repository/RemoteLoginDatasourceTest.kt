package soy.gabimoreno.data.remote.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.remote.datasource.login.RemoteLoginDatasource
import soy.gabimoreno.data.remote.model.AuthCookieApiModel
import soy.gabimoreno.data.remote.model.JwtAuthApiModel
import soy.gabimoreno.data.remote.model.MemberApiModel
import soy.gabimoreno.data.remote.model.StatusApiModel
import soy.gabimoreno.data.remote.service.LoginService

@ExperimentalCoroutinesApi
class RemoteLoginDatasourceTest {

    private val service: LoginService = mockk()
    private lateinit var repository: RemoteLoginDatasource

    @Before
    fun setUp() {
        repository = RemoteLoginDatasource(
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
            coEvery { service.generateAuthCookie(email, password) } throws Throwable()

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
            coEvery { service.obtainToken(username, password) } throws Throwable()

            val result = repository.obtainToken(username, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { service.obtainToken(username, password) }
        }

    @Test
    fun `GIVEN a success member WHEN getMember THEN get the expected result`() =
        runTest {
            val email = "email@example.com"
            val token = "1234"
            val members = buildMembers()
            coEvery { service.getMembers(email) } returns members

            val result = repository.getMember(email)

            result.isRight().shouldBeTrue()
            coVerifyOnce { service.getMembers(email) }
        }

    @Test
    fun `GIVEN a failure member WHEN getMember THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val token = "1234"
            coEvery { service.getMembers(email) } throws Throwable()

            val result = repository.getMember(email)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { service.getMembers(email) }
        }
}

private fun buildMembers() = listOf(
    MemberApiModel(
        statusApiModel = StatusApiModel.ACTIVE,
        isActive = true
    )
)
