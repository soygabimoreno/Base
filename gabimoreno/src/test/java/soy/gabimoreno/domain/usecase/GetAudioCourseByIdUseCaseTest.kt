package soy.gabimoreno.domain.usecase

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.fake.buildAudioCourse

class GetAudioCourseByIdUseCaseTest {

    private val repository = mockk<AudioCoursesRepository>()
    private lateinit var useCase: GetAudioCourseByIdUseCase

    @Before
    fun setUp() {
        useCase = GetAudioCourseByIdUseCase(repository)
    }

    @Test
    fun `GIVEN repository returns Right WHEN invoked THEN returns AudioCourse`() = runTest {
        val audioCourse = buildAudioCourse()
        coEvery { repository.getCourseById(audioCourse.id) } returns flowOf(audioCourse).right()

        val result = useCase(audioCourse.id)

        result.isRight() shouldBeEqualTo true
        result.getOrNull()!!.first() shouldBeEqualTo audioCourse
    }

    @Test
    fun `GIVEN repository returns Left WHEN invoked THEN returns error`() = runTest {
        val audioCourse = buildAudioCourse()
        val error = Throwable("Network error")
        coEvery { repository.getCourseById(audioCourse.id) } returns error.left()

        val result = useCase(audioCourse.id)

        result shouldBeEqualTo error.left()
    }
}
