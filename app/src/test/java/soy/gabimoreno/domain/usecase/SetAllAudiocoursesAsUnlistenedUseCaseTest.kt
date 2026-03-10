package soy.gabimoreno.domain.usecase

import android.content.Context
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.framework.datastore.getEmail

class SetAllAudiocoursesAsUnlistenedUseCaseTest {
    private val audioCoursesRepository: AudioCoursesRepository = relaxedMockk()
    private val context: Context = mockk()

    private lateinit var useCase: SetAllAudiocoursesAsUnlistenedUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = SetAllAudiocoursesAsUnlistenedUseCase(audioCoursesRepository, context)
    }

    @Test
    fun `GIVEN useCase WHEN invoke THEN repository is called`() =
        runTest {
            coEvery {
                audioCoursesRepository.markAllAudioCourseItemsAsUnlistened(EMAIL)
            } just Runs

            useCase()

            coVerifyOnce {
                audioCoursesRepository.markAllAudioCourseItemsAsUnlistened(EMAIL)
            }
        }
}

private const val EMAIL = "test@test.com"
