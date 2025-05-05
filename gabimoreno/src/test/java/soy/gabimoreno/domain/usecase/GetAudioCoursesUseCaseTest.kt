package soy.gabimoreno.domain.usecase

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository


class GetAudioCoursesUseCaseTest {

    private val repository = mockk<AudioCoursesRepository>()
    private lateinit var useCase: GetAudioCoursesUseCase

    @Before
    fun setUp() {
        useCase = GetAudioCoursesUseCase(repository)
    }

    @Test
    fun `GIVEN repository returns Right WHEN invoked THEN returns list of AudioCourse`() = runTest {
        val categories = listOf(Category.AUDIO_COURSES)
        val expectedCourses = listOf(relaxedMockk<AudioCourse>())
        coEvery { repository.getCourses(categories) } returns expectedCourses.right()

        val result = useCase(categories)

        result shouldBeEqualTo expectedCourses.right()
    }

    @Test
    fun `GIVEN repository returns Left WHEN invoked THEN returns error`() = runTest {
        val categories = listOf(Category.AUDIO_COURSES)
        val error = Throwable("Network error")
        coEvery { repository.getCourses(categories) } returns error.left()

        val result = useCase(categories)

        result shouldBeEqualTo error.left()
    }
}
