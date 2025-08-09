package soy.gabimoreno.domain.repository.podcast

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.domain.model.podcast.Episode

interface PodcastRepository {
    fun getEpisodesStream(email: String): Either<Throwable, Flow<List<Episode>>>
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
