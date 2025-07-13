package soy.gabimoreno.domain.usecase

import android.content.Context
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
import soy.gabimoreno.fake.buildPremiumAudio
import soy.gabimoreno.framework.datastore.getEmail

class MarkPremiumAudioAsListenedUseCaseTest {

    private val context: Context = mockk()
    private val repository = relaxedMockk<PremiumAudiosRepository>()

    private lateinit var useCase: MarkPremiumAudioAsListenedUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = MarkPremiumAudioAsListenedUseCase(context, repository)
    }

    @Test
    fun `GIVEN premiumAudio is listened WHEN invoke THEN repository is called`() = runTest {
        val premiumAudio = buildPremiumAudio()
        useCase(premiumAudio.id, true)

        coVerifyOnce {
            repository.markPremiumAudioAsListened(EMAIL, premiumAudio.id, true)
        }
    }

    @Test
    fun `GIVEN premiumAudio is unlistened WHEN invoke THEN repository is called`() = runTest {
        val premiumAudio = buildPremiumAudio()
        useCase(premiumAudio.id, false)

        coVerifyOnce {
            repository.markPremiumAudioAsListened(EMAIL, premiumAudio.id, false)
        }
    }
}

private const val EMAIL = "test@test.com"
