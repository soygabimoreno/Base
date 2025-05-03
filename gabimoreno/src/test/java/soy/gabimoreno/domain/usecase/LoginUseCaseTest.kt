package soy.gabimoreno.domain.usecase

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.remote.datasource.login.RemoteLoginDatasource
import soy.gabimoreno.domain.model.login.JwtAuth
import soy.gabimoreno.domain.model.login.Member
import soy.gabimoreno.domain.model.login.Status
import soy.gabimoreno.fake.buildAuthCookie
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import soy.gabimoreno.remoteconfig.TokenCredentials

@ExperimentalCoroutinesApi
class LoginUseCaseTest {

    private val repository: RemoteLoginDatasource = mockk()
    private val remoteConfigProvider: RemoteConfigProvider = mockk()
    private val setJwtAuthTokenUseCase: SetJwtAuthTokenUseCase = relaxedMockk()
    private val resetJwtAuthTokenUseCase: ResetJwtAuthTokenUseCase = relaxedMockk()
    private lateinit var useCase: LoginUseCase

    @Before
    fun setUp() {
        useCase = LoginUseCase(
            repository,
            remoteConfigProvider,
            setJwtAuthTokenUseCase,
            resetJwtAuthTokenUseCase,
        )
    }

    @Test
    fun `GIVEN the happy path WHEN invoke THEN get the expected result`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookie = buildAuthCookie()
            coEvery { repository.generateAuthCookie(email, password) } returns authCookie.right()

            val tokenCredentialUsername = "tokenCredentialUsername"
            val tokenCredentialPassword = "tokenCredentialPassword"
            val tokenCredentials =
                TokenCredentials(tokenCredentialUsername, tokenCredentialPassword)
            every { remoteConfigProvider.getTokenCredentials() } returns tokenCredentials

            val token = "token"
            val userEmail = "userEmail"
            val userNiceName = "userNiceName"
            val userDisplayName = "userDisplayName"
            val jwtAuth = JwtAuth(token, userEmail, userNiceName, userDisplayName)
            coEvery {
                repository.obtainToken(
                    tokenCredentialUsername,
                    tokenCredentialPassword
                )
            } returns jwtAuth.right()
            coEvery {
                repository.obtainToken(email, password)
            } returns jwtAuth.right()

            val member = Member(
                status = Status.ACTIVE,
                isActive = true
            )
            coEvery { repository.getMember(email) } returns member.right()

            val result = useCase(email, password)

            result.isRight().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
            coVerifyOnce {
                repository.obtainToken(
                    tokenCredentialUsername,
                    tokenCredentialPassword
                )
            }
            coVerifyOnce { repository.obtainToken(email, password) }
            coVerify(exactly = 2) { setJwtAuthTokenUseCase(token) }
            coVerifyOnce { repository.getMember(email) }
            coVerifyNever { resetJwtAuthTokenUseCase() }
        }

    @Test
    fun `GIVEN a failure authCookie WHEN invoke THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            coEvery { repository.generateAuthCookie(email, password) } returns Throwable().left()

            val result = useCase(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
        }

    @Test
    fun `GIVEN a not ok authCookie WHEN invoke THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookie = buildAuthCookie().copy(status = AUTH_COOKIE_STATUS_ERROR)
            coEvery { repository.generateAuthCookie(email, password) } returns authCookie.right()

            val result = useCase(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
        }

    @Test
    fun `GIVEN an empty tokenCredentialUsername WHEN invoke THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookie = buildAuthCookie()
            coEvery { repository.generateAuthCookie(email, password) } returns authCookie.right()

            val tokenCredentialPassword = "tokenCredentialPassword"
            val tokenCredentials =
                TokenCredentials(EMPTY_TOKEN_CREDENTIAL, tokenCredentialPassword)
            every { remoteConfigProvider.getTokenCredentials() } returns tokenCredentials

            val result = useCase(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
        }

    @Test
    fun `GIVEN an empty tokenCredentialPassword WHEN invoke THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookie = buildAuthCookie()
            coEvery { repository.generateAuthCookie(email, password) } returns authCookie.right()

            val tokenCredentialUsername = "tokenCredentialUsername"
            val tokenCredentials =
                TokenCredentials(tokenCredentialUsername, EMPTY_TOKEN_CREDENTIAL)
            every { remoteConfigProvider.getTokenCredentials() } returns tokenCredentials

            val result = useCase(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
        }

    @Test
    fun `GIVEN a failure token WHEN invoke THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookie = buildAuthCookie()
            coEvery { repository.generateAuthCookie(email, password) } returns authCookie.right()

            val tokenCredentialUsername = "tokenCredentialUsername"
            val tokenCredentialPassword = "tokenCredentialPassword"
            val tokenCredentials =
                TokenCredentials(tokenCredentialUsername, tokenCredentialPassword)
            every { remoteConfigProvider.getTokenCredentials() } returns tokenCredentials

            coEvery {
                repository.obtainToken(
                    tokenCredentialUsername,
                    tokenCredentialPassword
                )
            } returns Throwable().left()

            val result = useCase(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
        }

    @Test
    fun `GIVEN a failure member WHEN invoke THEN get the expected error`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookie = buildAuthCookie()
            coEvery { repository.generateAuthCookie(email, password) } returns authCookie.right()

            val tokenCredentialUsername = "tokenCredentialUsername"
            val tokenCredentialPassword = "tokenCredentialPassword"
            val tokenCredentials =
                TokenCredentials(tokenCredentialUsername, tokenCredentialPassword)
            every { remoteConfigProvider.getTokenCredentials() } returns tokenCredentials

            val token = "token"
            val userEmail = "userEmail"
            val userNiceName = "userNiceName"
            val userDisplayName = "userDisplayName"
            val jwtAuth = JwtAuth(token, userEmail, userNiceName, userDisplayName)
            coEvery {
                repository.obtainToken(
                    tokenCredentialUsername,
                    tokenCredentialPassword
                )
            } returns jwtAuth.right()
            coEvery {
                repository.obtainToken(email, password)
            } returns jwtAuth.right()

            coEvery { repository.getMember(email) } returns Throwable().left()

            val result = useCase(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
        }


    @Test
    fun `GIVEN expired token WHEN invoke THEN get the expected result`() =
        runTest {
            val email = "email@example.com"
            val password = "1234"
            val authCookie = buildAuthCookie()
            coEvery { repository.generateAuthCookie(email, password) } returns authCookie.right()

            val tokenCredentialUsername = "tokenCredentialUsername"
            val tokenCredentialPassword = "tokenCredentialPassword"
            val tokenCredentials =
                TokenCredentials(tokenCredentialUsername, tokenCredentialPassword)
            every { remoteConfigProvider.getTokenCredentials() } returns tokenCredentials

            coEvery {
                repository.obtainToken(
                    tokenCredentialUsername,
                    tokenCredentialPassword
                )
            } returns Throwable().left()

            val result = useCase(email, password)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.generateAuthCookie(email, password) }
            coVerifyOnce {
                repository.obtainToken(
                    tokenCredentialUsername,
                    tokenCredentialPassword
                )
            }
            coVerifyOnce { resetJwtAuthTokenUseCase() }
            coVerifyNever { repository.getMember(email) }
        }
}

private const val EMPTY_TOKEN_CREDENTIAL = ""
private const val AUTH_COOKIE_STATUS_ERROR = ""
