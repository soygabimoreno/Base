package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RefreshPremiumAudiosFromRemoteUseCaseTest {
    private val saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase:
        SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase = mockk()

    private val getLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase:
        GetLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase = mockk()

    private lateinit var useCase: RefreshPremiumAudiosFromRemoteUseCase

    @Before
    fun setUp() {
        useCase =
            RefreshPremiumAudiosFromRemoteUseCase(
                saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
                getLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
            )
    }

    @Test
    fun `GIVEN less elapsed time than time to refresh WHEN invoke THEN return false`() =
        runTest {
            val lastTimeInMillis = 0L
            val currentTimeInMillis = 1999L
            val timeToRefreshInMillis = 2000L
            coEvery { getLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase() } returns
                flowOf(
                    lastTimeInMillis,
                )
            coEvery {
                saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(currentTimeInMillis)
            } returns Unit

            val result =
                useCase(
                    currentTimeInMillis,
                    timeToRefreshInMillis,
                )

            result shouldBe false
        }

    @Test
    fun `GIVEN the same elapsed time than time to refresh WHEN invoke THEN return true`() =
        runTest {
            val lastTimeInMillis = 0L
            val currentTimeInMillis = 2000L
            val timeToRefreshInMillis = 2000L
            coEvery { getLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase() } returns
                flowOf(
                    lastTimeInMillis,
                )
            coEvery {
                saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(
                    currentTimeInMillis,
                )
            } returns Unit

            val result =
                useCase(
                    currentTimeInMillis,
                    timeToRefreshInMillis,
                )

            result shouldBe true
        }

    @Test
    fun `GIVEN more elapsed time than time to refresh WHEN invoke THEN return true`() =
        runTest {
            val lastTimeInMillis = 0L
            val currentTimeInMillis = 2001L
            val timeToRefreshInMillis = 2000L
            coEvery { getLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase() } returns
                flowOf(
                    lastTimeInMillis,
                )
            coEvery {
                saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(currentTimeInMillis)
            } returns Unit

            val result =
                useCase(
                    currentTimeInMillis,
                    timeToRefreshInMillis,
                )

            result shouldBe true
        }
}
