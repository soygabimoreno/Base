package soy.gabimoreno.domain.usecase

import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.framework.datastore.getEmail

class SetAllPremiumAudiosAsUnlistenedUseCaseTest {
    private val context: Context = mockk()
    private val premiumAudiosRepository: PremiumAudiosRepository = relaxedMockk()

    private lateinit var useCase: SetAllPremiumAudiosAsUnlistenedUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = SetAllPremiumAudiosAsUnlistenedUseCase(context, premiumAudiosRepository)
    }

    @Test
    fun `GIVEN useCase WHEN invoke THEN repository is called`() =
        runTest {
            coEvery {
                premiumAudiosRepository.markAllPremiumAudiosAsUnlistened(EMAIL)
            } returns Unit

            useCase()

            coVerifyOnce {
                premiumAudiosRepository.markAllPremiumAudiosAsUnlistened(EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
