package soy.gabimoreno.domain.repository

import soy.gabimoreno.domain.model.PodcastSearch
import soy.gabimoreno.error.Failure
import soy.gabimoreno.util.Either

interface PodcastRepository {

    suspend fun getPodcast(): Either<Failure, PodcastSearch> // TODO: Change this Either by the Arrow one
}
