package soy.gabimoreno.data.remote.datasource.podcast

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

interface PodcastDatasource {
    fun getEpisodesStream(podcastUrl: PodcastUrl): Either<Throwable, Flow<List<Episode>>>
    suspend fun getEpisodes(podcastUrl: PodcastUrl): Either<Throwable, EpisodesWrapper>
}
