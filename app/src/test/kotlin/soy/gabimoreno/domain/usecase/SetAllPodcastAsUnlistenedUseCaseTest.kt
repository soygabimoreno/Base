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
import soy.gabimoreno.framework.datastore.getEmail

class SetAllPodcastAsUnlistenedUseCaseTest {
    private val context: Context = mockk()
    private val podcastRepository: PodcastRepository = mockk()

    private lateinit var useCase: SetAllPodcastAsUnlistenedUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = SetAllPodcastAsUnlistenedUseCase(context, podcastRepository)
    }

    @Test
    fun `GIVEN useCase WHEN invoke THEN repository is called`() =
        runTest {
            coJustRun { podcastRepository.markAllPodcastAsUnlistened(EMAIL) }

            useCase()

            coVerifyOnce {
                podcastRepository.markAllPodcastAsUnlistened(EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
