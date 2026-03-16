package soy.gabimoreno.domain.repository.senioraudio

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.domain.model.content.SeniorAudio

interface SeniorAudioRepository {
    fun getSeniorAudiosStream(email: String): Either<Throwable, Flow<List<SeniorAudio>>>
    suspend fun getSeniorAudioById(podcastId: String): Either<Throwable, SeniorAudio>
    suspend fun getTotalSeniorAudios(): Either<Throwable, Int>
    suspend fun isEmpty(): Either<Throwable, Boolean>
    suspend fun reset()
}
