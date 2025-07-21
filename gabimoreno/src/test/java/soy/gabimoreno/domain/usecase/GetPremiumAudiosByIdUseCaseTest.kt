package soy.gabimoreno.domain.usecase

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.fake.buildPremiumAudios

@ExperimentalCoroutinesApi
class GetPremiumAudiosByIdUseCaseTest {
    private val premiumAudiosRepository: PremiumAudiosRepository = mockk()
    private lateinit var useCase: GetPremiumAudioByIdUseCase

    @Before
    fun setUp() {
        useCase =
            GetPremiumAudioByIdUseCase(
                premiumAudiosRepository = premiumAudiosRepository,
            )
    }

    @Test
    fun `GIVEN the happy path WHEN invoke THEN get the expected result`() =
        runTest {
            val premiumAudio = buildPremiumAudios().first()
            coEvery { premiumAudiosRepository.getPremiumAudioById(premiumAudio.id) } returns premiumAudio.right()

            val result = useCase(premiumAudio.id)

            result.isRight().shouldBeTrue()
            coVerifyOnce { premiumAudiosRepository.getPremiumAudioById(premiumAudio.id) }
        }

    @Test
    fun `GIVEN the error path WHEN invoke THEN get the expected error`() =
        runTest {
            val premiumAudio = buildPremiumAudios().first()
            coEvery { premiumAudiosRepository.getPremiumAudioById(premiumAudio.id) } returns Throwable().left()

            val result = useCase(premiumAudio.id)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { premiumAudiosRepository.getPremiumAudioById(premiumAudio.id) }
        }
}
