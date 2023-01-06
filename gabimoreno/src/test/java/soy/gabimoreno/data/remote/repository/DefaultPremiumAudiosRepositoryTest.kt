package soy.gabimoreno.data.remote.repository

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.local.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.repository.premiumaudios.DefaultPremiumAudiosRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase

@ExperimentalCoroutinesApi
class DefaultPremiumAudiosRepositoryTest {

    private val localPremiumAudiosDataSource: LocalPremiumAudiosDataSource = mockk()
    private val remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource = mockk()
    private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase =
        mockk()
    private lateinit var repository: DefaultPremiumAudiosRepository

    @Before
    fun setUp() {
        repository = DefaultPremiumAudiosRepository(
            localPremiumAudiosDataSource,
            remotePremiumAudiosDataSource,
            refreshPremiumAudiosFromRemoteUseCase
        )
    }

    @Test
    fun `GIVEN refreshPremiumAudiosFromRemoteUseCase true and success response WHEN getPremiumAudios THEN fetch from remote and save them locally`() =
        runTest {
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val premiumAudios: List<PremiumAudio> = relaxedMockk()
            coEvery { localPremiumAudiosDataSource.savePremiumAudios(premiumAudios) } returns Unit
            coEvery { remotePremiumAudiosDataSource.getPremiumAudios(categories) } returns premiumAudios.right()
            coEvery { localPremiumAudiosDataSource.getPremiumAudios() } returns premiumAudios

            repository.getPremiumAudios(categories)

            coVerifyOnce {
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
                localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
                localPremiumAudiosDataSource.getPremiumAudios()
            }

            coVerifyNever {
                localPremiumAudiosDataSource.isEmpty()
            }

            coVerifyOrder {
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
                localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
                localPremiumAudiosDataSource.getPremiumAudios()
            }
        }

    @Test
    fun `GIVEN refreshPremiumAudiosFromRemoteUseCase false and localPremiumAudiosDataSource empty and success response WHEN getPremiumAudios THEN fetch from remote and save them locally`() =
        runTest {
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localPremiumAudiosDataSource.isEmpty() } returns true
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val premiumAudios: List<PremiumAudio> = relaxedMockk()
            coEvery { localPremiumAudiosDataSource.savePremiumAudios(premiumAudios) } returns Unit
            coEvery { remotePremiumAudiosDataSource.getPremiumAudios(categories) } returns premiumAudios.right()
            coEvery { localPremiumAudiosDataSource.getPremiumAudios() } returns premiumAudios

            repository.getPremiumAudios(categories)

            coVerifyOnce {
                localPremiumAudiosDataSource.isEmpty()
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
                localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
                localPremiumAudiosDataSource.getPremiumAudios()
            }

            coVerifyOrder {
                localPremiumAudiosDataSource.isEmpty()
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
                localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
                localPremiumAudiosDataSource.getPremiumAudios()
            }
        }

    @Test
    fun `GIVEN refreshPremiumAudiosFromRemoteUseCase true and failure response WHEN getPremiumAudios THEN fetch from remote but does not save them locally`() =
        runTest {
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val premiumAudios: List<PremiumAudio> = relaxedMockk()
            coEvery { localPremiumAudiosDataSource.savePremiumAudios(premiumAudios) } returns Unit
            coEvery { remotePremiumAudiosDataSource.getPremiumAudios(categories) } returns Throwable().left()

            repository.getPremiumAudios(categories)

            coVerifyOnce {
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
            }

            coVerifyNever {
                localPremiumAudiosDataSource.isEmpty()
                localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
                localPremiumAudiosDataSource.getPremiumAudios()
            }

            coVerifyOrder {
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
            }
        }

    @Test
    fun `GIVEN refreshPremiumAudiosFromRemoteUseCase false and localPremiumAudiosDataSource empty and failure response WHEN getPremiumAudios THEN fetch from remote but does not save them locally`() =
        runTest {
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localPremiumAudiosDataSource.isEmpty() } returns true
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val premiumAudios: List<PremiumAudio> = relaxedMockk()
            coEvery { localPremiumAudiosDataSource.savePremiumAudios(premiumAudios) } returns Unit
            coEvery { remotePremiumAudiosDataSource.getPremiumAudios(categories) } returns Throwable().left()

            repository.getPremiumAudios(categories)

            coVerifyOnce {
                localPremiumAudiosDataSource.isEmpty()
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
            }

            coVerifyNever {
                localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
                localPremiumAudiosDataSource.getPremiumAudios()
            }

            coVerifyOrder {
                localPremiumAudiosDataSource.isEmpty()
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
            }
        }

    @Test
    fun `GIVEN refreshPremiumAudiosFromRemoteUseCase true and localPremiumAudiosDataSource filled WHEN getPremiumAudios THEN fetch from remote and save locally`() =
        runTest {
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localPremiumAudiosDataSource.isEmpty() } returns false
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val premiumAudios: List<PremiumAudio> = relaxedMockk()
            coEvery { localPremiumAudiosDataSource.getPremiumAudios() } returns premiumAudios

            repository.getPremiumAudios(categories)

            coVerifyOnce {
                localPremiumAudiosDataSource.isEmpty()
                localPremiumAudiosDataSource.getPremiumAudios()
            }

            coVerifyNever {
                remotePremiumAudiosDataSource.getPremiumAudios(categories)
                localPremiumAudiosDataSource.savePremiumAudios(premiumAudios)
            }

            coVerifyOrder {
                localPremiumAudiosDataSource.isEmpty()
                localPremiumAudiosDataSource.getPremiumAudios()
            }
        }
}
