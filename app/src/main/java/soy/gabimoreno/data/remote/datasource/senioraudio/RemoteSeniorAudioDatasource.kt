package soy.gabimoreno.data.remote.datasource.senioraudio

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode

interface RemoteSeniorAudioDatasource {
    fun getSeniorAudiosStream(podcastUrl: PodcastUrl): Either<Throwable, Flow<List<Episode>>>
}
