package soy.gabimoreno.data.remote.datasource.senioraudio

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.di.data.SeniorAudiosUrl
import soy.gabimoreno.domain.model.content.SeniorAudio

interface RemoteSeniorAudioDatasource {
    fun getSeniorAudiosStream(
        seniorAudiosUrl: SeniorAudiosUrl,
    ): Either<Throwable, Flow<List<SeniorAudio>>>
}
