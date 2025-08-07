package soy.gabimoreno.domain.repository.podcast

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

interface PodcastRepository {
    fun getEpisodesStream(): Either<Throwable, Flow<List<Episode>>>
    suspend fun getEpisodes(): Either<Throwable, EpisodesWrapper>
    suspend fun getPodcastById(podcastId: String): Either<Throwable, Episode>
    suspend fun getTotalPodcasts(): Either<Throwable, Int>
    suspend fun isEmpty(): Either<Throwable, Boolean>
    suspend fun markAllPodcastAsUnlistened(email: String)
    suspend fun markPodcastAsListened(
        podcastId: String,
        email: String,
        hasBeenListened: Boolean,
    )
    suspend fun reset()
    suspend fun updateMarkedAsFavorite(
        podcastId: String,
        email: String,
        isFavorite: Boolean,
    )
}
