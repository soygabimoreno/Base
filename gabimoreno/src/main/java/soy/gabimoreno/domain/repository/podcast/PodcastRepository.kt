package soy.gabimoreno.domain.repository.podcast

import arrow.core.Either
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

interface PodcastRepository {
    suspend fun getEpisodes(): Either<Throwable, EpisodesWrapper>
}
