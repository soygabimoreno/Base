package soy.gabimoreno.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.fake.buildPremiumAudio

class MarkPremiumAudioAsListenedUseCaseTest {

    private val repository = relaxedMockk<PremiumAudiosRepository>()

    private lateinit var useCase: MarkPremiumAudioAsListenedUseCase

    @Before
    fun setUp(){
        useCase = MarkPremiumAudioAsListenedUseCase(repository)
    }

    @Test
    fun `GIVEN premiumAudio is listened WHEN invoke THEN repository is called`() = runTest {
        val premiumAudio = buildPremiumAudio()
        useCase(premiumAudio.id, true)

        coVerifyOnce {
            repository.markPremiumAudioAsListened(premiumAudio.id, true)
        }
    }

    @Test
    fun `GIVEN premiumAudio is unlistened WHEN invoke THEN repository is called`() = runTest {
        val premiumAudio = buildPremiumAudio()
        useCase(premiumAudio.id, false)

        coVerifyOnce {
            repository.markPremiumAudioAsListened(premiumAudio.id, false)
        }
    }
}
