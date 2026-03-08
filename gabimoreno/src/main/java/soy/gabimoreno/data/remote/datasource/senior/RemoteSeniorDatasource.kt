package soy.gabimoreno.data.remote.datasource.senior

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode

interface RemoteSeniorDatasource {
    fun getEpisodesStream(podcastUrl: PodcastUrl): Either<Throwable, Flow<List<Episode>>>
}
