package soy.gabimoreno.data.remote.datasource.podcast

import arrow.core.Either
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

interface PodcastDatasource {
    suspend fun getEpisodes(podcastUrl: PodcastUrl): Either<Throwable, EpisodesWrapper>
}
