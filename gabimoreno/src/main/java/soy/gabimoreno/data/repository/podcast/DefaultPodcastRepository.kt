package soy.gabimoreno.data.repository.podcast

import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soy.gabimoreno.data.local.podcast.LocalPodcastDataSource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.di.IOScope
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPodcastRepository
    @Inject
    constructor(
        private val localPodcastDatasource: LocalPodcastDataSource,
        private val podcastUrl: PodcastUrl,
        private val remotePodcastRepository: PodcastDatasource,
        @param:IOScope private val scope: CoroutineScope,
    ) : PodcastRepository {
        override fun getEpisodesStream(): Either<Throwable, Flow<List<Episode>>> =
            when (val remoteResult = remotePodcastRepository.getEpisodesStream(podcastUrl)) {
                is Either.Left -> Either.Right(localPodcastDatasource.getPodcasts())
                is Either.Right -> {
                    remoteResult.value
                        .onEach { episodes -> localPodcastDatasource.upsertPodcasts(episodes) }
                        .launchIn(scope)
                    Either.Right(localPodcastDatasource.getPodcasts())
                }
            }

        override suspend fun getEpisodes(): Either<Throwable, EpisodesWrapper> {
            TODO("Not yet implemented")
        }

        override suspend fun getPodcastById(podcastId: String): Either<Throwable, Episode> {
            TODO("Not yet implemented")
        }

        override suspend fun getTotalPodcasts(): Either<Throwable, Int> {
            TODO("Not yet implemented")
        }

        override suspend fun isEmpty(): Either<Throwable, Boolean> {
            TODO("Not yet implemented")
        }

        override suspend fun markAllPodcastAsUnlistened(email: String) {
            TODO("Not yet implemented")
        }

        override suspend fun markPodcastAsListened(
            podcastId: String,
            email: String,
            hasBeenListened: Boolean,
        ) {
            TODO("Not yet implemented")
        }

        override suspend fun reset() {
            TODO("Not yet implemented")
        }

        override suspend fun updateMarkedAsFavorite(
            podcastId: String,
            email: String,
            isFavorite: Boolean,
        ) {
            TODO("Not yet implemented")
        }
    }
