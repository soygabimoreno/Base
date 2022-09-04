package soy.gabimoreno.domain.usecase

import arrow.core.Either
import arrow.core.left
import soy.gabimoreno.domain.model.login.Member
import soy.gabimoreno.domain.repository.GabiMorenoRepository
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: GabiMorenoRepository,
    private val remoteConfigProvider: RemoteConfigProvider
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Either<Throwable, Member> {
        return repository.generateAuthCookie(email, password)
            .fold(
                {
                    it.left()
                }, {
                    // TODO: Check if it is possible to get a success without a `"status": "ok"`
                    val tokenCredentials = remoteConfigProvider.getTokenCredentials()
                    val tokenCredentialUsername = tokenCredentials.username
                    val tokenCredentialPassword = tokenCredentials.password
                    repository.obtainToken(tokenCredentialUsername, tokenCredentialPassword)
                        .fold(
                            {
                                it.left()
                            },
                            { jwtAuth ->
                                val token = jwtAuth.token
                                repository.getMember(email, token)
                            }
                        )
                }
            )
    }
}
