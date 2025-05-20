package soy.gabimoreno.domain.usecase

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository

class SetAllAudiocoursesAsUnlistenedUseCaseTest {

    private val audioCoursesRepository: AudioCoursesRepository = relaxedMockk()
    private lateinit var useCase: SetAllAudiocoursesAsUnlistenedUseCase

    @Before
    fun setUp() {
        useCase = SetAllAudiocoursesAsUnlistenedUseCase(audioCoursesRepository)
    }

    @Test
    fun `GIVEN useCase WHEN invoke THEN repository is called`() = runTest {
        coEvery {
            audioCoursesRepository.markAllAudioCourseItemsAsUnlistened()
        } just Runs

        useCase()

        coVerifyOnce {
            audioCoursesRepository.markAllAudioCourseItemsAsUnlistened()
        }
    }
}
