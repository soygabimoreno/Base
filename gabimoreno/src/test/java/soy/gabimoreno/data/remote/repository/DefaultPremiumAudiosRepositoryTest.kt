package soy.gabimoreno.data.remote.repository

import androidx.paging.PagingSource
import arrow.core.Either
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.cloud.audiosync.datasource.PremiumAudiosCloudDataSource
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.repository.premiumaudios.DefaultPremiumAudiosRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.fake.buildPremiumAudio
import soy.gabimoreno.fake.buildPremiumAudioDbModel

@ExperimentalCoroutinesApi
class DefaultPremiumAudiosRepositoryTest {

    private val cloudDataSource: PremiumAudiosCloudDataSource = mockk()
    private val localPremiumAudiosDataSource = relaxedMockk<LocalPremiumAudiosDataSource>()
    private val remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource = mockk()
    private val refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase =
        mockk()
    private val saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase: SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase =
        mockk()
    private lateinit var repository: DefaultPremiumAudiosRepository

    @Before
    fun setUp() {
        repository = DefaultPremiumAudiosRepository(
            cloudDataSource,
            localPremiumAudiosDataSource,
            remotePremiumAudiosDataSource,
            refreshPremiumAudiosFromRemoteUseCase,
            saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
        )
    }

    @Test
    fun `GIVEN valid categories WHEN getPremiumAudioMediator THEN callback is called`() = runTest {
        val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
        val pagingSource: PagingSource<Int, PremiumAudioDbModel> = relaxedMockk()
        coEvery { localPremiumAudiosDataSource.getPremiumAudiosPagingSource() } returns pagingSource

        val result = repository.getPremiumAudioMediator(categories, EMAIL)

        result shouldBeInstanceOf Either.Right::class.java
    }

    @Test
    fun `GIVEN exception in paging WHEN getPremiumAudioMediator THEN error is returned`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS)
            coEvery { localPremiumAudiosDataSource.getPremiumAudiosPagingSource() } throws RuntimeException(
                "Unexpected error"
            )

            val result = repository.getPremiumAudioMediator(categories, EMAIL)

            assert(result is Either.Right)
            val flow = (result as Either.Right).value
            runCatching {
                flow.collect {}
            }.isFailure shouldBe true
        }

    @Test
    fun `GIVEN audio is listened WHEN markPremiumAudioAsListened THEN field is updated`() =
        runTest {
            val premiumAudio = buildPremiumAudio()
            coJustRun {
                cloudDataSource.upsertPremiumAudioItemFields(
                    EMAIL, premiumAudio.id, mapOf(
                        "id" to premiumAudio.id,
                        "hasBeenListened" to true
                    )
                )
            }

            repository.markPremiumAudioAsListened(EMAIL, premiumAudio.id, true)

            coVerifyOnce {
                localPremiumAudiosDataSource.updateHasBeenListened(premiumAudio.id, true)
                cloudDataSource.upsertPremiumAudioItemFields(
                    EMAIL, premiumAudio.id, mapOf(
                        "id" to premiumAudio.id,
                        "hasBeenListened" to true
                    )
                )
            }
        }

    @Test
    fun `GIVEN audio is unlistened WHEN markPremiumAudioAsListened THEN field is updated`() =
        runTest {
            val premiumAudio = buildPremiumAudio()
            coJustRun {
                cloudDataSource.upsertPremiumAudioItemFields(
                    EMAIL, premiumAudio.id, mapOf(
                        "id" to premiumAudio.id,
                        "hasBeenListened" to false
                    )
                )
            }

            repository.markPremiumAudioAsListened(EMAIL, premiumAudio.id, false)

            coVerifyOnce {
                localPremiumAudiosDataSource.updateHasBeenListened(premiumAudio.id, false)
                cloudDataSource.upsertPremiumAudioItemFields(
                    EMAIL, premiumAudio.id, mapOf(
                        "id" to premiumAudio.id,
                        "hasBeenListened" to false
                    )
                )
            }
        }

    @Test
    fun `GIVEN repository WHEN markAllPremiumAudiosAsUnlistened THEN local dataSource is called`() =
        runTest {
            coEvery {
                localPremiumAudiosDataSource.markAllPremiumAudiosAsUnlistened()
            } returns Unit
            coJustRun {
                cloudDataSource.batchUpdateFieldsForAllPremiumAudioItems(
                    EMAIL,
                    mapOf("hasBeenListened" to false)
                )
            }

            repository.markAllPremiumAudiosAsUnlistened(EMAIL)

            coVerifyOnce {
                localPremiumAudiosDataSource.markAllPremiumAudiosAsUnlistened()
                cloudDataSource.batchUpdateFieldsForAllPremiumAudioItems(
                    EMAIL,
                    mapOf("hasBeenListened" to false)
                )
            }
        }

    @Test
    fun `GIVEN repository WHEN getAllFavoritePremiumAudios THEN local dataSource is called`() =
        runTest {
            val premiumAudios = listOf(
                buildPremiumAudioDbModel("1", markedAsFavorite = true),
            )
            coEvery {
                localPremiumAudiosDataSource.getAllFavoritePremiumAudios()
            } returns premiumAudios

            val result = repository.getAllFavoritePremiumAudios()

            result shouldBeInstanceOf Either.Right::class.java
            coVerifyOnce {
                localPremiumAudiosDataSource.getAllFavoritePremiumAudios()
            }
        }

    @Test
    fun `GIVEN audio is marked as listened WHEN updateMarkedAsFavorite THEN field is updated`() =
        runTest {
            val premiumAudio = buildPremiumAudio()
            coJustRun {
                cloudDataSource.upsertPremiumAudioItemFields(
                    EMAIL, premiumAudio.id, mapOf(
                        "id" to premiumAudio.id,
                        "markedAsFavorite" to true
                    )
                )
            }

            repository.markPremiumAudioAsFavorite(EMAIL, premiumAudio.id, true)

            coVerifyOnce {
                localPremiumAudiosDataSource.updateMarkedAsFavorite(premiumAudio.id, true)
                cloudDataSource.upsertPremiumAudioItemFields(
                    EMAIL, premiumAudio.id, mapOf(
                        "id" to premiumAudio.id,
                        "markedAsFavorite" to true
                    )
                )
            }
        }
}

private const val EMAIL = "test@test.com"
