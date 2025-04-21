package soy.gabimoreno.domain.repository.podcast

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

interface PodcastRepository {
    fun getEpisodesStream(): Either<Throwable, Flow<List<Episode>>>
    suspend fun getEpisodes(): Either<Throwable, EpisodesWrapper>
}
