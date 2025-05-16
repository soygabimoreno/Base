package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
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
            audioCoursesRepository.markAllAudioCourseItemAsUnlistened()
        } returns Unit

        useCase()

        coVerifyOnce {
            audioCoursesRepository.markAllAudioCourseItemAsUnlistened()
        }
    }
}
