package soy.gabimoreno.data.repository.podcast

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soy.gabimoreno.data.cloud.audiosync.datasource.PodcastCloudDataSource
import soy.gabimoreno.data.local.podcast.LocalPodcastDataSource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.di.IOScope
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPodcastRepository
    @Inject
    constructor(
        private val cloudDataSource: PodcastCloudDataSource,
        private val localPodcastDatasource: LocalPodcastDataSource,
        private val podcastUrl: PodcastUrl,
        private val remotePodcastRepository: PodcastDatasource,
        @param:IOScope private val scope: CoroutineScope,
    ) : PodcastRepository {
        override fun getEpisodesStream(email: String): Either<Throwable, Flow<List<Episode>>> =
            when (val remoteResult = remotePodcastRepository.getEpisodesStream(podcastUrl)) {
                is Either.Left -> Either.Right(localPodcastDatasource.getPodcasts())
                is Either.Right -> {
                    remoteResult.value
                        .onEach { episodes ->
                            val mergedEpisodes =
                                if (email.isNotEmpty()) {
                                    mergeWithCloudData(episodes, email)
                                } else {
                                    mergeWithLocalData(episodes)
                                }
                            localPodcastDatasource.upsertPodcasts(mergedEpisodes)
                        }.launchIn(scope)

                    Either.Right(localPodcastDatasource.getPodcasts())
                }
            }

        override suspend fun getPodcastById(podcastId: String): Either<Throwable, Episode> =
            localPodcastDatasource.getPodcastById(podcastId).let { podcast ->
                when (podcast) {
                    null -> Either.Left(Throwable("Podcast not found"))
                    else -> Either.Right(podcast)
                }
            }

        override suspend fun getTotalPodcasts(): Either<Throwable, Int> =
            localPodcastDatasource.getTotalPodcasts().right()

        override suspend fun isEmpty(): Either<Throwable, Boolean> =
            localPodcastDatasource.isEmpty().right()

        override suspend fun markAllPodcastAsUnlistened(email: String) {
            if (email.isNotEmpty()) {
                cloudDataSource.batchUpdateFieldsForAllPodcastItems(
                    email,
                    mapOf(HAS_BEEN_LISTENED to false),
                )
            }
            localPodcastDatasource.markAllPodcastAsUnlistened()
        }

        override suspend fun markPodcastAsListened(
            podcastId: String,
            email: String,
            hasBeenListened: Boolean,
        ) {
            if (email.isNotEmpty()) {
                cloudDataSource.upsertPodcastFields(
                    email,
                    podcastId,
                    mapOf(
                        AUDIO_ID to podcastId,
                        HAS_BEEN_LISTENED to hasBeenListened,
                    ),
                )
            }
            localPodcastDatasource.updateHasBeenListened(podcastId, hasBeenListened)
        }

        override suspend fun reset() {
            localPodcastDatasource.reset()
        }

        override suspend fun updateMarkedAsFavorite(
            podcastId: String,
            email: String,
            isFavorite: Boolean,
        ) {
            if (email.isNotEmpty()) {
                cloudDataSource.upsertPodcastFields(
                    email,
                    podcastId,
                    mapOf(
                        AUDIO_ID to podcastId,
                        MARKED_AS_FAVORITE to isFavorite,
                    ),
                )
            }
            localPodcastDatasource.updateMarkedAsFavorite(podcastId, isFavorite)
        }

        private suspend fun mergeWithCloudData(
            remoteEpisodes: List<Episode>,
            email: String,
        ): List<Episode> {
            val cloudSnapshot =
                cloudDataSource
                    .getPodcastItems(email)
                    .associateBy { it.id }

            return remoteEpisodes.map { remoteItem ->
                val cloudItem = cloudSnapshot[remoteItem.id]
                remoteItem.copy(
                    hasBeenListened = cloudItem?.hasBeenListened ?: false,
                    markedAsFavorite = cloudItem?.markedAsFavorite ?: false,
                )
            }
        }

        private suspend fun mergeWithLocalData(remoteEpisodes: List<Episode>): List<Episode> {
            val localSnapshot =
                localPodcastDatasource
                    .getPodcasts()
                    .first()
                    .associateBy { it.id }

            return remoteEpisodes.map { remoteItem ->
                val localItem = localSnapshot[remoteItem.id]
                remoteItem.copy(
                    hasBeenListened = localItem?.hasBeenListened ?: false,
                    markedAsFavorite = localItem?.markedAsFavorite ?: false,
                )
            }
        }
    }

private const val AUDIO_ID = "id"
private const val HAS_BEEN_LISTENED = "hasBeenListened"
private const val MARKED_AS_FAVORITE = "markedAsFavorite"
