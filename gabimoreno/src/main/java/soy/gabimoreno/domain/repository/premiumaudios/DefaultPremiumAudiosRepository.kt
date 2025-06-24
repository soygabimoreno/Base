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
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.local.mapper.toPremiumAudio
import soy.gabimoreno.data.remote.datasource.premiumaudios.PremiumAudiosRemoteMediator
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPremiumAudiosRepository @Inject constructor(
    private val localPremiumAudiosDataSource: LocalPremiumAudiosDataSource,
    private val remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource,
    private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
    private val saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase: SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
) : PremiumAudiosRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPremiumAudioMediator(
        categories: List<Category>,
    ): Either<Throwable, Flow<PagingData<PremiumAudio>>> {
        return try {
            val pager = Pager(
                config = PagingConfig(
                    pageSize = MAX_ITEMS,
                    prefetchDistance = PREFETCH_ITEMS,
                    initialLoadSize = MAX_ITEMS * 2,
                ),
                remoteMediator = PremiumAudiosRemoteMediator(
                    localPremiumAudiosDataSource = localPremiumAudiosDataSource,
                    remotePremiumAudiosDataSource = remotePremiumAudiosDataSource,
                    refreshPremiumAudiosFromRemoteUseCase = refreshPremiumAudiosFromRemoteUseCase,
                    saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase = saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
                ),
                pagingSourceFactory = {
                    localPremiumAudiosDataSource.getPremiumAudiosPagingSource()
                }
            )

            (pager.flow.map { pagingData ->
                pagingData.map { it.toPremiumAudio() }
            }).right()
        } catch (e: Exception) {
            e.left()
        }
    }

    override suspend fun markPremiumAudioAsListened(id: String, hasBeenListened: Boolean) {
        localPremiumAudiosDataSource.updateHasBeenListened(id, hasBeenListened)
    }

    override suspend fun getPremiumAudioById(idPremiumAudio: String): Either<Throwable, PremiumAudio> {
        return localPremiumAudiosDataSource.getPremiumAudioById(idPremiumAudio)?.right()
            ?: Throwable("PremiumAudio not found").left()
    }

    override suspend fun markAllPremiumAudiosAsUnlistened() {
        return localPremiumAudiosDataSource.markAllPremiumAudiosAsUnlistened()
    }

    suspend fun reset() {
        localPremiumAudiosDataSource.reset()
    }
}

private const val MAX_ITEMS = 10
private const val PREFETCH_ITEMS = 20
internal const val TWELVE_HOURS_IN_MILLIS = 12 * 60 * 60 * 1000L
