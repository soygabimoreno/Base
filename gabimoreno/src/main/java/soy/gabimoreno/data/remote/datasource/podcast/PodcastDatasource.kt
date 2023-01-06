package soy.gabimoreno.data.remote.datasource.podcast

import arrow.core.Either
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

interface PodcastDatasource {
    suspend fun getEpisodes(url: String): Either<Throwable, EpisodesWrapper>
}
