package soy.gabimoreno.domain.repository

import arrow.core.Either
import soy.gabimoreno.domain.model.podcast.PodcastSearch

interface PodcastRepository {

    suspend fun getEpisodes(): Either<Throwable, PodcastSearch>
}
