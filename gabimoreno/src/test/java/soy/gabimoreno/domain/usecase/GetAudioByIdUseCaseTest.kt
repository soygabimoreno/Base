package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildAudioItem
import soy.gabimoreno.fake.buildEpisode
import soy.gabimoreno.fake.buildPremiumAudio

class GetAudioByIdUseCaseTest {
    private val audioCoursesRepository: AudioCoursesRepository = mockk()
    private val podcastRepository: PodcastRepository = mockk()
    private val premiumAudiosRepository: PremiumAudiosRepository = mockk()

    private lateinit var useCase: GetAudioByIdUseCase

    @Before
    fun setUp() {
        useCase =
            GetAudioByIdUseCase(
                audioCoursesRepository,
                podcastRepository,
                premiumAudiosRepository,
            )
    }

    @Test
    fun `GIVEN audioCourseId WHEN invoke THEN audioCoursesRepository is called`() =
        runTest {
            val audioId = "123-1"
            val audioCourseItem = buildAudioItem(audioId)
            val expected = right(audioCourseItem)
            coEvery { audioCoursesRepository.getAudioCourseItemById(audioId) } returns expected

            val result = useCase(audioId)

            result shouldBeEqualTo expected
            coVerifyOnce {
                audioCoursesRepository.getAudioCourseItemById(audioId)
            }
            coVerifyNever {
                premiumAudiosRepository.getPremiumAudioById(any())
                podcastRepository.getPodcastById(any())
            }
        }

    @Test
    fun `GIVEN premiumAudioId WHEN invoke THEN premiumAudiosRepository is called`() =
        runTest {
            val audioId = "1"
            val premiumAudio = buildPremiumAudio(audioId)
            val expected = right(premiumAudio)
            coEvery { premiumAudiosRepository.getPremiumAudioById(audioId) } returns expected

            val result = useCase(audioId)

            result shouldBeEqualTo expected
            coVerifyOnce {
                premiumAudiosRepository.getPremiumAudioById(audioId)
            }
            coVerifyNever {
                audioCoursesRepository.getAudioCourseItemById(any())
                podcastRepository.getPodcastById(any())
            }
        }

    @Test
    fun `GIVEN podcastId WHEN invoke THEN podcastRepository is called`() =
        runTest {
            val audioId = "123e4567-e89b-12d3-a456-426614174000"
            val podcast = buildEpisode(audioId)
            val expected = right(podcast)
            coEvery { podcastRepository.getPodcastById(audioId) } returns expected

            val result = useCase(audioId)

            result shouldBeEqualTo expected
            coVerifyOnce {
                podcastRepository.getPodcastById(audioId)
            }
            coVerifyNever {
                audioCoursesRepository.getAudioCourseItemById(any())
                premiumAudiosRepository.getPremiumAudioById(any())
            }
        }
}
