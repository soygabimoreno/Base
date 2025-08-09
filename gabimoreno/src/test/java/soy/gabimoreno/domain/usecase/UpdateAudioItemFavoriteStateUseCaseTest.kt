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
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.fake.buildAudioCourseItem
import soy.gabimoreno.fake.buildEpisode
import soy.gabimoreno.fake.buildPremiumAudio
import soy.gabimoreno.framework.datastore.getEmail

class UpdateAudioItemFavoriteStateUseCaseTest {
    private val audioRepository = mockk<AudioCoursesRepository>()
    private val context: Context = mockk()
    private val podcastRepository = mockk<PodcastRepository>()
    private val premiumRepository = mockk<PremiumAudiosRepository>()

    private lateinit var useCase: UpdateAudioItemFavoriteStateUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase =
            UpdateAudioItemFavoriteStateUseCase(
                audioRepository,
                context,
                podcastRepository,
                premiumRepository,
            )
    }

    @Test
    fun `GIVEN id contain a AudioCourse format WHEN invoked THEN audioCoursesRepository is called`() =
        runTest {
            val audioCourseItem = buildAudioCourseItem(markedAsFavorite = false)
            coJustRun {
                audioRepository.updateMarkedAsFavorite(
                    audioCourseItem.id,
                    EMAIL,
                    true,
                )
            }

            useCase(
                audioCourseItem.id,
                true,
            )

            coVerifyOnce {
                audioRepository.updateMarkedAsFavorite(audioCourseItem.id, EMAIL, true)
            }
            coVerifyNever {
                premiumRepository.markPremiumAudioAsFavorite(EMAIL, any(), any())
                podcastRepository.updateMarkedAsFavorite(any(), EMAIL, any())
            }
        }

    @Test
    fun `GIVEN id contain a PremiumAudio format WHEN invoked THEN premiumAudioRepository is called`() =
        runTest {
            val premiumAudio = buildPremiumAudio(markedAsFavorite = false)
            coJustRun {
                premiumRepository.markPremiumAudioAsFavorite(
                    EMAIL,
                    premiumAudio.id,
                    true,
                )
            }

            useCase(premiumAudio.id, true)

            coVerifyOnce {
                premiumRepository.markPremiumAudioAsFavorite(EMAIL, premiumAudio.id, true)
            }
            coVerifyNever {
                audioRepository.updateMarkedAsFavorite(any(), EMAIL, any())
                podcastRepository.updateMarkedAsFavorite(any(), EMAIL, any())
            }
        }

    @Test
    fun `GIVEN id contain a Podcast format WHEN invoked THEN podcastRepository is called`() =
        runTest {
            val podcastAudio =
                buildEpisode(
                    id = "d332a4c2-4282-45c0-9ccc-2aeaab8df48d",
                    markedAsFavorite = false,
                )
            coJustRun {
                podcastRepository.updateMarkedAsFavorite(
                    podcastAudio.id,
                    EMAIL,
                    true,
                )
            }

            useCase(podcastAudio.id, true)

            coVerifyOnce {
                podcastRepository.updateMarkedAsFavorite(podcastAudio.id, EMAIL, true)
            }
            coVerifyNever {
                audioRepository.updateMarkedAsFavorite(any(), EMAIL, any())
                premiumRepository.markPremiumAudioAsFavorite(EMAIL, any(), any())
            }
        }
}

private const val EMAIL = "test@test.com"
