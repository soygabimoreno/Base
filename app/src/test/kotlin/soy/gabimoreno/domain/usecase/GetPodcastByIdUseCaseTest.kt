package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildEpisode

class GetPodcastByIdUseCaseTest {
    private val repository: PodcastRepository = mockk()

    private lateinit var useCase: GetPodcastByIdUseCase

    @Before
    fun setUp() {
        useCase = GetPodcastByIdUseCase(repository)
    }

    @Test
    fun `GIVEN valid podcast id WHEN invoke THEN returns right with podcast`() =
        runTest {
            val podcast = buildEpisode()
            val podcastId = podcast.id
            coEvery { repository.getPodcastById(podcastId) } returns right(podcast)

            val result = useCase(podcastId)

            result shouldBeEqualTo right(podcast)
            coVerifyOnce {
                repository.getPodcastById(podcastId)
            }
        }

    @Test
    fun `GIVEN invalid podcast id WHEN invoke THEN returns left`() =
        runTest {
            val podcast = buildEpisode()
            val podcastId = podcast.id
            val exception = RuntimeException("Database error")
            coEvery { repository.getPodcastById(podcastId) } returns left(exception)

            val result = useCase(podcastId)

            result shouldBeEqualTo left(exception)
            coVerifyOnce {
                repository.getPodcastById(podcastId)
            }
        }
}
