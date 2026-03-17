package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.Either
import arrow.core.left
import kotlinx.coroutines.flow.first
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.framework.datastore.getEmail
import soy.gabimoreno.framework.datastore.getPassword
import javax.inject.Inject

class RefreshBearerTokenUseCase
    @Inject
    constructor(
        private val loginDatasource: LoginDatasource,
        private val setJwtAuthTokenUseCase: SetJwtAuthTokenUseCase,
        private val resetJwtAuthTokenUseCase: ResetJwtAuthTokenUseCase,
        private val context: Context,
    ) {
        suspend operator fun invoke(): Either<Throwable, Unit> {
            val email = context.getEmail().first()
            val password = context.getPassword().first()
            if (email.isEmpty() || password.isEmpty()) {
                return expireToken()
            }
            return loginDatasource
                .obtainToken(email, password)
                .map { jwtAuth ->
                    val token = jwtAuth.token
                    setJwtAuthTokenUseCase(token)
                }.map {
                    expireToken()
                }
        }

        private suspend fun expireToken(): Either<TokenExpiredException, Nothing> {
            resetJwtAuthTokenUseCase()
            return TokenExpiredException().left()
        }
    }
