@file:OptIn(ExperimentalPagingApi::class)

package soy.gabimoreno.data.remote.datasource.premiumaudios

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.InitializeAction
import androidx.paging.RemoteMediator.MediatorResult
import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.repository.premiumaudios.TWELVE_HOURS_IN_MILLIS
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.fake.buildPremiumAudios
import java.io.IOException

@ExperimentalCoroutinesApi
class PremiumAudiosRemoteMediatorTest {

    private val localPremiumAudiosDataSource: LocalPremiumAudiosDataSource = relaxedMockk()
    private val remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource = relaxedMockk()
    private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase =
        relaxedMockk()
    private val saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase:
        SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase = relaxedMockk()

    private lateinit var mediator: PremiumAudiosRemoteMediator

    @Before
    fun setUp() {
        mediator = PremiumAudiosRemoteMediator(
            localPremiumAudiosDataSource,
            remotePremiumAudiosDataSource,
            refreshPremiumAudiosFromRemoteUseCase,
            saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
        )

        coEvery {
            remotePremiumAudiosDataSource.getPremiumAudios(
                any(),
                any(),
                any()
            )
        } returns buildPremiumAudios().right()
    }

    @Test
    fun `GIVEN refresh needed WHEN initialize THEN launch initial refresh`() = runTest {
        coEvery {
            refreshPremiumAudiosFromRemoteUseCase(
                any(),
                TWELVE_HOURS_IN_MILLIS
            )
        } returns true

        val result = mediator.initialize()

        result shouldBe InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    @Test
    fun `GIVEN refresh not needed WHEN initialize THEN skip initial refresh`() = runTest {
        coEvery {
            refreshPremiumAudiosFromRemoteUseCase(
                any(),
                TWELVE_HOURS_IN_MILLIS
            )
        } returns false

        val result = mediator.initialize()

        result shouldBe InitializeAction.SKIP_INITIAL_REFRESH
    }

    @Test
    fun `WHEN load PREPEND THEN endOfPaginationReached is true`() = runTest {
        val result = mediator.load(LoadType.PREPEND, emptyPagingState)

        assertMediatorSuccess(result, true)
    }

    @Test
    fun `GIVEN success on REFRESH WHEN load THEN reset and return success`() = runTest {
        val premiumAudios = buildPremiumAudios()

        coEvery {
            remotePremiumAudiosDataSource.getPremiumAudios(categories, PAGE_SIZE, 1)
        } returns premiumAudios.right()
        coEvery { localPremiumAudiosDataSource.reset() } returns Unit
        coEvery { saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(any()) } returns Unit
        coEvery { localPremiumAudiosDataSource.savePremiumAudios(premiumAudios) } returns Unit

        val result = mediator.load(LoadType.REFRESH, emptyPagingState)

        assertMediatorSuccess(result, premiumAudios.isEmpty())

        coVerifyOnce {
            localPremiumAudiosDataSource.reset()
            saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(any())
            localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
        }
    }

    @Test
    fun `GIVEN error from API WHEN load REFRESH THEN return MediatorResult Error`() = runTest {
        val error = IOException("Network error")

        coEvery {
            remotePremiumAudiosDataSource.getPremiumAudios(categories, PAGE_SIZE, 1)
        } returns error.left()

        val result = mediator.load(LoadType.REFRESH, emptyPagingState)

        result shouldBeInstanceOf MediatorResult.Error::class
        (result as MediatorResult.Error).throwable shouldBe error
    }

    @Test
    fun `GIVEN exception thrown WHEN load THEN return Error`() = runTest {
        val error = IOException("Thrown")

        coEvery {
            remotePremiumAudiosDataSource.getPremiumAudios(categories, PAGE_SIZE, 1)
        } throws error

        val result = mediator.load(LoadType.REFRESH, emptyPagingState)

        result shouldBeInstanceOf MediatorResult.Error::class
        (result as MediatorResult.Error).throwable shouldBe error
    }

    @Test
    fun `GIVEN items in database WHEN load APPEND THEN fetch next page`() = runTest {
        val premiumAudios = buildPremiumAudios()
        val totalAudios = 25

        coEvery { localPremiumAudiosDataSource.getTotalPremiumAudios() } returns totalAudios
        coEvery {
            remotePremiumAudiosDataSource.getPremiumAudios(categories, PAGE_SIZE, 2)
        } returns premiumAudios.right()
        coEvery { localPremiumAudiosDataSource.savePremiumAudios(premiumAudios) } returns Unit

        val result = mediator.load(LoadType.APPEND, pagingStateWithItem())

        assertMediatorSuccess(result, premiumAudios.isEmpty())

        coVerifyOnce {
            remotePremiumAudiosDataSource.getPremiumAudios(categories, PAGE_SIZE, 3)
        }
    }
}

private fun pagingStateWithItem(): PagingState<Int, PremiumAudioDbModel> {
    val item = mockk<PremiumAudioDbModel>()
    return PagingState(
        pages = listOf(
            PagingSource.LoadResult.Page(
                data = listOf(item),
                prevKey = null,
                nextKey = null
            )
        ),
        anchorPosition = 0,
        config = PagingConfig(pageSize = PAGE_SIZE),
        leadingPlaceholderCount = 0
    )
}

private fun assertMediatorSuccess(result: MediatorResult, endReached: Boolean) {
    result shouldBeInstanceOf MediatorResult.Success::class
    (result as MediatorResult.Success).endOfPaginationReached shouldBe endReached
}

private val emptyPagingState = PagingState<Int, PremiumAudioDbModel>(
    pages = listOf(),
    anchorPosition = null,
    config = PagingConfig(pageSize = PAGE_SIZE),
    leadingPlaceholderCount = 0
)

private const val PAGE_SIZE = 20
private val categories = listOf(Category.PREMIUM)
