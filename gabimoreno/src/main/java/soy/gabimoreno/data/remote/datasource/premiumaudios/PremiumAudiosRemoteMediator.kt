package soy.gabimoreno.data.remote.datasource.premiumaudios

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.repository.premiumaudios.TWELVE_HOURS_IN_MILLIS
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import java.io.IOException
import kotlin.math.ceil

@OptIn(ExperimentalPagingApi::class)
class PremiumAudiosRemoteMediator(
    private val localPremiumAudiosDataSource: LocalPremiumAudiosDataSource,
    private val remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource,
    private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
    private val saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase: SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
) : RemoteMediator<Int, PremiumAudioDbModel>() {

    override suspend fun initialize(): InitializeAction {
        return if (refreshPremiumAudiosFromRemoteUseCase(
                currentTimeInMillis = System.currentTimeMillis(),
                timeToRefreshInMillis = TWELVE_HOURS_IN_MILLIS
            )
        ) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PremiumAudioDbModel>
    ): MediatorResult {
        val categories = listOf(Category.PREMIUM)

        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val totalAudios = localPremiumAudiosDataSource.getTotalPremiumAudios()
                    ceil(totalAudios.toDouble() / state.config.pageSize.toDouble()).toInt() + 1
                }
            }

            val response = remotePremiumAudiosDataSource.getPremiumAudios(
                categories = categories,
                postsPerPage = state.config.pageSize,
                page = currentPage,
            )

            return response.fold({
                MediatorResult.Error(it)
            }, { premiumAudios ->
                if (loadType == LoadType.REFRESH) {
                    localPremiumAudiosDataSource.reset()
                    saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(
                        System.currentTimeMillis()
                    )
                }
                localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
                MediatorResult.Success(
                    endOfPaginationReached = premiumAudios.isEmpty()
                )
            })
        } catch (e: IOException) {
            MediatorResult.Error(e)
        }
    }
}

private const val STARTING_PAGE_INDEX = 1
