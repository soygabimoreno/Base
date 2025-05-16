package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository


class SetAllPremiumAudiosAsUnlistenedUseCaseTest {
    private val premiumAudiosRepository: PremiumAudiosRepository = relaxedMockk()
    private lateinit var useCase: SetAllPremiumAudiosAsUnlistenedUseCase

    @Before
    fun setUp() {
        useCase = SetAllPremiumAudiosAsUnlistenedUseCase(premiumAudiosRepository)
    }

    @Test
    fun `GIVEN useCase WHEN invoke THEN repository is called`() = runTest {
        coEvery {
            premiumAudiosRepository.markAllPremiumAudiosAsUnlistened()
        } returns Unit

        useCase()

        coVerifyOnce {
            premiumAudiosRepository.markAllPremiumAudiosAsUnlistened()
        }
    }
}
