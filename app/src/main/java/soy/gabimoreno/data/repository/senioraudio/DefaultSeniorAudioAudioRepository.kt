package soy.gabimoreno.data.repository.senioraudio

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soy.gabimoreno.data.local.podcast.LocalSeniorAudioDataSource
import soy.gabimoreno.data.remote.datasource.senioraudio.RemoteSeniorAudioDatasource
import soy.gabimoreno.di.IOScope
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.repository.senioraudio.SeniorAudioRepository
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSeniorAudioAudioRepository
    @Inject
    constructor(
        private val localSeniorAudioDataSource: LocalSeniorAudioDataSource,
        private val remoteSeniorAudioDataSource: RemoteSeniorAudioDatasource,
        private val remoteConfigProvider: RemoteConfigProvider,
        @param:IOScope private val scope: CoroutineScope,
    ) : SeniorAudioRepository {
        override fun getEpisodesStream(email: String): Either<Throwable, Flow<List<Episode>>> =
            when (
                val remoteResult =
                    remoteSeniorAudioDataSource.getSeniorAudiosStream(
                        remoteConfigProvider.getSeniorRssUrl())
            ) {
                is Either.Left -> Either.Right(localSeniorAudioDataSource.getseniorAudios())
                is Either.Right -> {
                    remoteResult.value
                        .onEach { episodes ->
                            localSeniorAudioDataSource.upsertPodcasts(mergeWithLocalData(episodes))
                        }.launchIn(scope)

                    Either.Right(localSeniorAudioDataSource.getseniorAudios())
                }
            }

        override suspend fun getPodcastById(podcastId: String): Either<Throwable, Episode> =
            localSeniorAudioDataSource.getPodcastById(podcastId).let { podcast ->
                when (podcast) {
                    null -> Either.Left(Throwable("Podcast not found"))
                    else -> Either.Right(podcast)
                }
            }

        override suspend fun getTotalPodcasts(): Either<Throwable, Int> =
            localSeniorAudioDataSource.getTotalSeniorAudios().right()

        override suspend fun isEmpty(): Either<Throwable, Boolean> =
            localSeniorAudioDataSource.isEmpty().right()

        override suspend fun reset() {
            localSeniorAudioDataSource.reset()
        }

        private suspend fun mergeWithLocalData(remoteEpisodes: List<Episode>): List<Episode> {
            val localSnapshot =
                localSeniorAudioDataSource
                    .getseniorAudios()
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
