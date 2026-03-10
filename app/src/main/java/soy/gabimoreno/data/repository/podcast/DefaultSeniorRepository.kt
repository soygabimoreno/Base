package soy.gabimoreno.data.repository.podcast

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soy.gabimoreno.data.local.podcast.LocalSeniorDataSource
import soy.gabimoreno.data.remote.datasource.senior.RemoteSeniorDatasource
import soy.gabimoreno.di.IOScope
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.repository.senior.SeniorRepository
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSeniorRepository
    @Inject
    constructor(
        private val localSeniorDataSource: LocalSeniorDataSource,
        private val remoteSeniorDataSource: RemoteSeniorDatasource,
        private val remoteConfigProvider: RemoteConfigProvider,
        @param:IOScope private val scope: CoroutineScope,
    ) : SeniorRepository {
        override fun getEpisodesStream(email: String): Either<Throwable, Flow<List<Episode>>> =
            when (
                val remoteResult =
                    remoteSeniorDataSource.getEpisodesStream(remoteConfigProvider.getSeniorRssUrl())
            ) {
                is Either.Left -> Either.Right(localSeniorDataSource.getPodcasts())
                is Either.Right -> {
                    remoteResult.value
                        .onEach { episodes ->
                            localSeniorDataSource.upsertPodcasts(mergeWithLocalData(episodes))
                        }.launchIn(scope)

                    Either.Right(localSeniorDataSource.getPodcasts())
                }
            }

        override suspend fun getPodcastById(podcastId: String): Either<Throwable, Episode> =
            localSeniorDataSource.getPodcastById(podcastId).let { podcast ->
                when (podcast) {
                    null -> Either.Left(Throwable("Podcast not found"))
                    else -> Either.Right(podcast)
                }
            }

        override suspend fun getTotalPodcasts(): Either<Throwable, Int> =
            localSeniorDataSource.getTotalPodcasts().right()

        override suspend fun isEmpty(): Either<Throwable, Boolean> =
            localSeniorDataSource.isEmpty().right()

        override suspend fun reset() {
            localSeniorDataSource.reset()
        }

        private suspend fun mergeWithLocalData(remoteEpisodes: List<Episode>): List<Episode> {
            val localSnapshot =
                localSeniorDataSource
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
