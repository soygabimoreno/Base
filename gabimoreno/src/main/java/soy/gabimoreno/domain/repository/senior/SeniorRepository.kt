package soy.gabimoreno.domain.repository.senior

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.domain.model.podcast.Episode

interface SeniorRepository {
    fun getEpisodesStream(email: String): Either<Throwable, Flow<List<Episode>>>
    suspend fun getPodcastById(podcastId: String): Either<Throwable, Episode>
    suspend fun getTotalPodcasts(): Either<Throwable, Int>
    suspend fun isEmpty(): Either<Throwable, Boolean>
    suspend fun reset()
}
