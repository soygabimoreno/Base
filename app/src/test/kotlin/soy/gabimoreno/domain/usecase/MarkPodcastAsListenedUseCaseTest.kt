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
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.fake.buildEpisode
import soy.gabimoreno.framework.datastore.getEmail

class MarkPodcastAsListenedUseCaseTest {
    private val context: Context = mockk()
    private val repository: PodcastRepository = mockk()

    private lateinit var useCase: MarkPodcastAsListenedUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = MarkPodcastAsListenedUseCase(context, repository)
    }

    @Test
    fun `GIVEN podcastId is listened WHEN invoke THEN repository is called`() =
        runTest {
            val podcast = buildEpisode()
            coJustRun { repository.markPodcastAsListened(podcast.id, EMAIL, true) }

            useCase(podcast.id, true)

            coVerifyOnce {
                repository.markPodcastAsListened(podcast.id, EMAIL, true)
            }
        }

    @Test
    fun `GIVEN podcastId is unlistened WHEN invoke THEN repository is called`() =
        runTest {
            val podcast = buildEpisode()
            coJustRun { repository.markPodcastAsListened(podcast.id, EMAIL, false) }

            useCase(podcast.id, false)

            coVerifyOnce {
                repository.markPodcastAsListened(podcast.id, EMAIL, false)
            }
        }
}

private const val EMAIL = "test@test.com"
