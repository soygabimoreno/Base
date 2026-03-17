package soy.gabimoreno.data.repository.senioraudio

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soy.gabimoreno.data.local.senioraudio.LocalSeniorAudioDataSource
import soy.gabimoreno.data.remote.datasource.senioraudio.RemoteSeniorAudioDatasource
import soy.gabimoreno.di.IOScope
import soy.gabimoreno.domain.model.content.SeniorAudio
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
        override fun getSeniorAudiosStream(
            email: String,
        ): Either<Throwable, Flow<List<SeniorAudio>>> =
            when (
                val remoteResult =
                    remoteSeniorAudioDataSource.getSeniorAudiosStream(
                        seniorAudiosUrl = remoteConfigProvider.getSeniorRssUrl(),
                    )
            ) {
                is Either.Left -> {
                    Either.Right(localSeniorAudioDataSource.getSeniorAudios())
                }

                is Either.Right -> {
                    remoteResult.value
                        .onEach { seniorAudios ->
                            localSeniorAudioDataSource.upsertSeniorAudios(
                                mergeWithLocalData(
                                    seniorAudios,
                                ),
                            )
                        }.launchIn(scope)

                    Either.Right(localSeniorAudioDataSource.getSeniorAudios())
                }
            }

        override suspend fun getSeniorAudioById(
            seniorAudioId: String,
        ): Either<Throwable, SeniorAudio> =
            localSeniorAudioDataSource.getSeniorAudioById(seniorAudioId).let { seniorAudio ->
                when (seniorAudio) {
                    null -> Either.Left(Throwable("Senior Audio not found"))
                    else -> Either.Right(seniorAudio)
                }
            }

        override suspend fun getTotalSeniorAudios(): Either<Throwable, Int> =
            localSeniorAudioDataSource.getTotalSeniorAudios().right()

        override suspend fun isEmpty(): Either<Throwable, Boolean> =
            localSeniorAudioDataSource.isEmpty().right()

        override suspend fun reset() {
            localSeniorAudioDataSource.reset()
        }

        private suspend fun mergeWithLocalData(seniorAudios: List<SeniorAudio>): List<SeniorAudio> {
            val localSnapshot =
                localSeniorAudioDataSource
                    .getSeniorAudios()
                    .first()
                    .associateBy { it.id }

            return seniorAudios.map { remoteItem ->
                val localItem = localSnapshot[remoteItem.id]
                remoteItem.copy(
                    hasBeenListened = localItem?.hasBeenListened ?: false,
                    markedAsFavorite = localItem?.markedAsFavorite ?: false,
                )
            }
        }
    }
