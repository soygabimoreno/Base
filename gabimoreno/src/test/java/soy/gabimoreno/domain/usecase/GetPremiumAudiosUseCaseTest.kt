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
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.fake.buildPremiumAudios

@ExperimentalCoroutinesApi
class GetPremiumAudiosUseCaseTest {

    private val premiumAudiosRepository: PremiumAudiosRepository = mockk()
    private lateinit var useCase: GetPremiumAudiosUseCase

    @Before
    fun setUp() {
        useCase = GetPremiumAudiosUseCase(
            premiumAudiosRepository
        )
    }

    @Test
    fun `GIVEN the happy path WHEN invoke THEN get the expected result`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val premiumAudios = buildPremiumAudios()
            coEvery { premiumAudiosRepository.getPremiumAudios(categories) } returns premiumAudios.right()

            val result = useCase(categories)

            result.isRight().shouldBeTrue()
            coVerifyOnce { premiumAudiosRepository.getPremiumAudios(categories) }
        }

    @Test
    fun `GIVEN a failure posts WHEN invoke THEN get the expected error`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            coEvery { premiumAudiosRepository.getPremiumAudios(categories) } returns Throwable().left()

            val result = useCase(categories)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { premiumAudiosRepository.getPremiumAudios(categories) }
        }
}
