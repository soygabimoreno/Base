package soy.gabimoreno.domain.usecase

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.model.login.Member
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import soy.gabimoreno.remoteconfig.TokenCredentials
import javax.inject.Inject

class LoginUseCase
    @Inject
    constructor(
        private val loginDatasource: LoginDatasource,
        private val remoteConfigProvider: RemoteConfigProvider,
        private val setJwtAuthTokenUseCase: SetJwtAuthTokenUseCase,
        private val resetJwtAuthTokenUseCase: ResetJwtAuthTokenUseCase,
    ) {
        suspend operator fun invoke(
            email: String,
            password: String,
        ): Either<Throwable, Member> =
            loginDatasource
                .generateAuthCookie(email, password)
                .flatMap { authCookie: AuthCookie ->
                    validateAuthCookie(authCookie)
                }.flatMap {
                    obtainAndSetToken()
                }.flatMap {
                    getMemberAndSetCredentials(email, password)
                }

        private fun validateAuthCookie(authCookie: AuthCookie): Either<Throwable, Unit> =
            if (authCookie.isStatusOK()) {
                Unit.right()
            } else {
                Throwable("Invalid auth cookie").left()
            }

        private suspend fun obtainAndSetToken(): Either<Throwable, Unit> {
            val tokenCredentials = remoteConfigProvider.getTokenCredentials()

            return validateTokenCredentials(tokenCredentials)
                .flatMap {
                    loginDatasource.obtainToken(
                        tokenCredentials.username,
                        tokenCredentials.password,
                    )
                }.map { jwtAuth ->
                    setJwtAuthTokenUseCase(jwtAuth.token)
                }.mapLeft {
                    resetJwtAuthTokenUseCase()
                    TokenExpiredException()
                }
        }

        private fun validateTokenCredentials(
            credentials: TokenCredentials,
        ): Either<Throwable, Unit> =
            when {
                credentials.username.isBlank() ->
                    Throwable("Username cannot be blank").left()

                credentials.password.isBlank() ->
                    Throwable("Password cannot be blank").left()

                else -> Unit.right()
            }

        private suspend fun getMemberAndSetCredentials(
            email: String,
            password: String,
        ): Either<Throwable, Member> =
            loginDatasource.getMember(email).also {
                setUserToken(email, password)
            }

        private suspend fun setUserToken(
            email: String,
            password: String,
        ) {
            loginDatasource
                .obtainToken(email, password)
                .map { jwtAuth ->
                    val token = jwtAuth.token
                    setJwtAuthTokenUseCase(token)
                }.mapLeft {
                    resetJwtAuthTokenUseCase()
                    TokenExpiredException().left()
                }
        }
    }
