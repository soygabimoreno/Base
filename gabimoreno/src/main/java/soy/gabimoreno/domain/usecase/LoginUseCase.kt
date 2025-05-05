package soy.gabimoreno.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.model.login.Member
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginDatasource: LoginDatasource,
    private val remoteConfigProvider: RemoteConfigProvider,
    private val setJwtAuthTokenUseCase: SetJwtAuthTokenUseCase,
    private val resetJwtAuthTokenUseCase: ResetJwtAuthTokenUseCase,
) {

    suspend operator fun invoke(
        email: String,
        password: String,
    ): Either<Throwable, Member> {
        return loginDatasource.generateAuthCookie(email, password)
            .map { authCookie ->
                if (!authCookie.isStatusOK()) return Throwable().left()
                val tokenCredentials = remoteConfigProvider.getTokenCredentials()
                val tokenCredentialUsername = tokenCredentials.username
                val tokenCredentialPassword = tokenCredentials.password
                if (tokenCredentialUsername.isBlank()) return Throwable().left()
                if (tokenCredentialPassword.isBlank()) return Throwable().left()

                return loginDatasource.obtainToken(tokenCredentialUsername, tokenCredentialPassword)
                    .map { jwtAuth ->
                        val token = jwtAuth.token
                        setJwtAuthTokenUseCase(token)
                        val member = loginDatasource.getMember(email)
                        setUserToken(email, password)
                        return member
                    }.mapLeft {
                        resetJwtAuthTokenUseCase()
                        return TokenExpiredException().left()
                    }
            }
    }

    private suspend fun setUserToken(email: String, password: String) {
        loginDatasource.obtainToken(email, password)
            .map { jwtAuth ->
                val token = jwtAuth.token
                setJwtAuthTokenUseCase(token)
            }.mapLeft {
                resetJwtAuthTokenUseCase()
                TokenExpiredException().left()
            }
    }
}
