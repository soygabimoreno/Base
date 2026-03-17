package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.model.content.SeniorAudio
import soy.gabimoreno.domain.repository.senioraudio.SeniorAudioRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class GetSeniorAudioStreamUseCase
    @Inject
    constructor(
        private val context: Context,
        private val seniorAudioRepository: SeniorAudioRepository,
    ) {
        suspend operator fun invoke(): Either<Throwable, Flow<List<SeniorAudio>>> {
            val email = context.getEmail().first()
            return seniorAudioRepository.getSeniorAudiosStream(
                email = email,
            )
        }
    }
