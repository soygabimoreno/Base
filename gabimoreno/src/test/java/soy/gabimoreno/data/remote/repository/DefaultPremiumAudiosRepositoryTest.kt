package soy.gabimoreno.data.remote.repository

import androidx.paging.PagingSource
import arrow.core.Either
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
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.repository.premiumaudios.DefaultPremiumAudiosRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.fake.buildPremiumAudio

@ExperimentalCoroutinesApi
class DefaultPremiumAudiosRepositoryTest {

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

        val result = repository.getPremiumAudioMediator(categories)

        result shouldBeInstanceOf Either.Right::class.java
    }

    @Test
    fun `GIVEN exception in paging WHEN getPremiumAudioMediator THEN error is returned`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS)
            coEvery { localPremiumAudiosDataSource.getPremiumAudiosPagingSource() } throws RuntimeException(
                "Unexpected error"
            )

            val result = repository.getPremiumAudioMediator(categories)

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
            repository.markPremiumAudioAsListened(premiumAudio.id, true)

            coVerifyOnce {
                localPremiumAudiosDataSource.updateHasBeenListened(premiumAudio.id, true)
            }
        }

    @Test
    fun `GIVEN audio is unlistened WHEN markPremiumAudioAsListened THEN field is updated`() =
        runTest {
            val premiumAudio = buildPremiumAudio()
            repository.markPremiumAudioAsListened(premiumAudio.id, false)

            coVerifyOnce {
                localPremiumAudiosDataSource.updateHasBeenListened(premiumAudio.id, false)
            }
        }
}
