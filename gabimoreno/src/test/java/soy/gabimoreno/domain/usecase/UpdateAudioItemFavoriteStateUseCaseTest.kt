package soy.gabimoreno.domain.usecase

import android.content.Context
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.fake.buildAudioCourseItem
import soy.gabimoreno.fake.buildPremiumAudio
import soy.gabimoreno.framework.datastore.getEmail

class UpdateAudioItemFavoriteStateUseCaseTest {

    private val audioRepository = mockk<AudioCoursesRepository>()
    private val context: Context = mockk()
    private val premiumRepository = mockk<PremiumAudiosRepository>()

    private lateinit var useCase: UpdateAudioItemFavoriteStateUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = UpdateAudioItemFavoriteStateUseCase(audioRepository, context, premiumRepository)
    }

    @Test
    fun `GIVEN id contains dash WHEN invoked THEN audioCoursesRepository is called`() = runTest {
        val audioCourseItem = buildAudioCourseItem(markedAsFavorite = false)
        coJustRun {
            audioRepository.updateMarkedAsFavorite(
                audioCourseItem.id,
                EMAIL,
                true
            )
        }

        useCase(
            audioCourseItem.id,
            true
        )

        coVerifyOnce {
            audioRepository.updateMarkedAsFavorite(audioCourseItem.id, EMAIL, true)
        }
        coVerifyNever {
            premiumRepository.markPremiumAudioAsFavorite(EMAIL, any(), any())
        }
    }

    @Test
    fun `GIVEN id does not contain dash WHEN invoked THEN premiumAudioRepository is called`() =
        runTest {
            val premiumAudio = buildPremiumAudio(markedAsFavorite = false)
            coJustRun {
                premiumRepository.markPremiumAudioAsFavorite(
                    EMAIL,
                    premiumAudio.id,
                    true
                )
            }

            useCase(premiumAudio.id, true)

            coVerifyOnce {
                premiumRepository.markPremiumAudioAsFavorite(EMAIL, premiumAudio.id, true)
            }
            coVerifyNever {
                audioRepository.updateMarkedAsFavorite(any(), EMAIL, any())
            }
        }
}

private const val EMAIL = "test@test.com"
