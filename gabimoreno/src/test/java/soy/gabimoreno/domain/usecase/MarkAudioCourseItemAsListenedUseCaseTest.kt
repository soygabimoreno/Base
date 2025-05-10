package soy.gabimoreno.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.fake.buildAudioCourse

class MarkAudioCourseItemAsListenedUseCaseTest {

    private val repository = relaxedMockk<AudioCoursesRepository>()

    private lateinit var useCase: MarkAudioCourseItemAsListenedUseCase

    @Before
    fun setUp() {
        useCase = MarkAudioCourseItemAsListenedUseCase(repository)
    }

    @Test
    fun `GIVEN item is listened WHEN invoke THEN repository is called`() = runTest {
        val audioCourse = buildAudioCourse()
        useCase(audioCourse.id, true)

        coVerifyOnce {
            repository.markAudioCourseItemAsListened(audioCourse.id, true)
        }
    }

    @Test
    fun `GIVEN item is unlistened WHEN invoke THEN repository is called`() = runTest {
        val audioCourse = buildAudioCourse()
        useCase(audioCourse.id, false)

        coVerifyOnce {
            repository.markAudioCourseItemAsListened(audioCourse.id, false)
        }
    }
}
