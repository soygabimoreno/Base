package soy.gabimoreno.domain.repository.premiumaudios

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import soy.gabimoreno.data.cloud.audiosync.datasource.PremiumAudiosCloudDataSource
import soy.gabimoreno.data.local.mapper.toPremiumAudio
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.premiumaudios.PremiumAudiosRemoteMediator
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPremiumAudiosRepository
    @Inject
    constructor(
        private val cloudDataSource: PremiumAudiosCloudDataSource,
        private val localPremiumAudiosDataSource: LocalPremiumAudiosDataSource,
        private val remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource,
        private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
        private val saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase:
            SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
    ) : PremiumAudiosRepository {
        @OptIn(ExperimentalPagingApi::class)
        override suspend fun getPremiumAudioMediator(
            categories: List<Category>,
            email: String,
        ): Either<Throwable, Flow<PagingData<PremiumAudio>>> =
            try {
                val pager =
                    Pager(
                        config =
                            PagingConfig(
                                pageSize = MAX_ITEMS,
                                prefetchDistance = PREFETCH_ITEMS,
                                initialLoadSize = MAX_ITEMS * 2,
                            ),
                        remoteMediator =
                            PremiumAudiosRemoteMediator(
                                cloudDataSource = cloudDataSource,
                                email = email,
                                localPremiumAudiosDataSource = localPremiumAudiosDataSource,
                                remotePremiumAudiosDataSource = remotePremiumAudiosDataSource,
                                refreshPremiumAudiosFromRemoteUseCase =
                                refreshPremiumAudiosFromRemoteUseCase,
                                saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase =
                                saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
                            ),
                        pagingSourceFactory = {
                            localPremiumAudiosDataSource.getPremiumAudiosPagingSource()
                        },
                    )

                (
                    pager.flow.map { pagingData ->
                        pagingData.map { it.toPremiumAudio() }
                    }
                ).right()
            } catch (e: Exception) {
                e.left()
            }

        override suspend fun markPremiumAudioAsListened(
            email: String,
            premiumAudioId: String,
            hasBeenListened: Boolean,
        ) {
            if (email.isNotEmpty()) {
                cloudDataSource.upsertPremiumAudioItemFields(
                    email,
                    premiumAudioId,
                    mapOf(
                        "id" to premiumAudioId,
                        "hasBeenListened" to hasBeenListened,
                    ),
                )
            }
            localPremiumAudiosDataSource.updateHasBeenListened(premiumAudioId, hasBeenListened)
        }

        override suspend fun getPremiumAudioById(
            premiumAudioId: String,
        ): Either<Throwable, PremiumAudio> =
            localPremiumAudiosDataSource.getPremiumAudioById(premiumAudioId)?.right()
                ?: Throwable("PremiumAudio not found").left()

        override suspend fun markAllPremiumAudiosAsUnlistened(email: String) {
            if (email.isNotEmpty()) {
                cloudDataSource.batchUpdateFieldsForAllPremiumAudioItems(
                    email,
                    mapOf("hasBeenListened" to false),
                )
            }
            return localPremiumAudiosDataSource.markAllPremiumAudiosAsUnlistened()
        }

        override suspend fun getAllFavoritePremiumAudios(): Either<Throwable, List<PremiumAudio>> =
            localPremiumAudiosDataSource.getAllFavoritePremiumAudios()?.let { premiumAudios ->
                premiumAudios.map { it.toPremiumAudio() }.right()
            } ?: Throwable("PremiumAudios not found").left()

        override suspend fun markPremiumAudioAsFavorite(
            email: String,
            premiumAudioId: String,
            isFavorite: Boolean,
        ) {
            if (email.isNotEmpty()) {
                cloudDataSource.upsertPremiumAudioItemFields(
                    email,
                    premiumAudioId,
                    mapOf(
                        "id" to premiumAudioId,
                        "markedAsFavorite" to isFavorite,
                    ),
                )
            }
            return localPremiumAudiosDataSource.updateMarkedAsFavorite(premiumAudioId, isFavorite)
        }

        suspend fun reset() {
            localPremiumAudiosDataSource.reset()
        }
    }

private const val MAX_ITEMS = 10
private const val PREFETCH_ITEMS = 20
internal const val TWELVE_HOURS_IN_MILLIS = 12 * 60 * 60 * 1000L
