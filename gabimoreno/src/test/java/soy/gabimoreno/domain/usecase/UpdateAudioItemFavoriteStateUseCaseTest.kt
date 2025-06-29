package soy.gabimoreno.domain.usecase

import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.fake.buildAudioCourseItem
import soy.gabimoreno.fake.buildPremiumAudio

class UpdateAudioItemFavoriteStateUseCaseTest {

    private val audioRepository = mockk<AudioCoursesRepository>()
    private val premiumRepository = mockk<PremiumAudiosRepository>()
    private lateinit var useCase: UpdateAudioItemFavoriteStateUseCase

    @Before
    fun setUp() {
        useCase = UpdateAudioItemFavoriteStateUseCase(audioRepository, premiumRepository)
    }

    @Test
    fun `GIVEN id contains dash WHEN invoked THEN audioCoursesRepository is called`() = runTest {
        val audioCourseItem = buildAudioCourseItem(markedAsFavorite = false)
        coJustRun {
            audioRepository.updateMarkedAsFavorite(
                audioCourseItem.id,
                true
            )
        }

        useCase(
            audioCourseItem.id,
            true
        )

        coVerifyOnce {
            audioRepository.updateMarkedAsFavorite(audioCourseItem.id, true)
        }
        coVerifyNever {
            premiumRepository.markPremiumAudioAsFavorite(any(), any())
        }
    }

    @Test
    fun `GIVEN id does not contain dash WHEN invoked THEN premiumAudioRepository is called`() =
        runTest {
            val premiumAudio = buildPremiumAudio(markedAsFavorite = false)
            coJustRun {
                premiumRepository.markPremiumAudioAsFavorite(
                    premiumAudio.id,
                    true
                )
            }

            useCase(premiumAudio.id, true)

            coVerifyOnce {
                premiumRepository.markPremiumAudioAsFavorite(premiumAudio.id, true)
            }
            coVerifyNever {
                audioRepository.updateMarkedAsFavorite(any(), any())
            }
        }
}
