package soy.gabimoreno.data.remote.repository

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.local.audiocourses.LocalAudioCoursesDataSource
import soy.gabimoreno.data.remote.datasource.audiocourses.RemoteAudioCoursesDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCoursesRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase

class DefaultAudioCoursesRepositoryTest {

    private val localAudioCoursesDataSource = relaxedMockk<LocalAudioCoursesDataSource>()
    private val remoteAudioCoursesDataSource = relaxedMockk<RemoteAudioCoursesDataSource>()
    private val refreshPremiumAudiosFromRemoteUseCase =
        mockk<RefreshPremiumAudiosFromRemoteUseCase>()
    private lateinit var repository: DefaultAudioCoursesRepository

    @Before
    fun setUp() {
        repository = DefaultAudioCoursesRepository(
            localAudioCoursesDataSource,
            remoteAudioCoursesDataSource,
            refreshPremiumAudiosFromRemoteUseCase
        )
    }

    @Test
    fun `GIVEN refresh is true WHEN getCourses THEN fetch from remote and save locally`() =
        runTest {
            val categories = listOf(Category.AUDIO_COURSES)
            val courses: List<AudioCourse> = relaxedMockk()
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns courses.right()
            coEvery { localAudioCoursesDataSource.getAudioCourses() } returns courses

            val result = repository.getCourses(listOf(Category.AUDIO_COURSES))

            result shouldBeEqualTo courses.right()
            coVerifyOnce {
                remoteAudioCoursesDataSource.getAudioCourses(any())
                localAudioCoursesDataSource.saveAudioCourses(courses)
            }
        }

    @Test
    fun `GIVEN local is empty WHEN getCourses THEN fetch from remote`() = runTest {
        val categories = listOf(Category.AUDIO_COURSES)
        val courses = listOf(relaxedMockk<AudioCourse>())
        coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
        coEvery { localAudioCoursesDataSource.isEmpty() } returns true
        coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns courses.right()
        coEvery { localAudioCoursesDataSource.getAudioCourses() } returns courses

        val result = repository.getCourses(listOf(Category.AUDIO_COURSES))

        result shouldBeEqualTo courses.right()
        coVerifyOnce { remoteAudioCoursesDataSource.getAudioCourses(categories) }
        coVerifyOnce { localAudioCoursesDataSource.saveAudioCourses(courses) }
    }

    @Test
    fun `GIVEN remote fails WHEN getCourses THEN return error`() = runTest {
        val categories = listOf(Category.AUDIO_COURSES)
        val error = Throwable("Network error")
        coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
        coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns error.left()

        val result = repository.getCourses(listOf(Category.AUDIO_COURSES))

        result shouldBeEqualTo error.left()
    }

    @Test
    fun `GIVEN no refresh and local is not empty WHEN getCourses THEN fetch from local only`() =
        runTest {
            val categories = listOf(Category.AUDIO_COURSES)
            val courses = listOf(relaxedMockk<AudioCourse>())
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCoursesDataSource.isEmpty() } returns false
            coEvery { localAudioCoursesDataSource.getAudioCourses() } returns courses

            val result = repository.getCourses(listOf(Category.AUDIO_COURSES))

            result shouldBeEqualTo courses.right()
            coVerifyNever { remoteAudioCoursesDataSource.getAudioCourses(categories) }
        }

    @Test
    fun `GIVEN valid course id WHEN getCourseById THEN return course`() = runTest {
        val course = relaxedMockk<AudioCourse>()
        coEvery { localAudioCoursesDataSource.getAudioCourseById("id") } returns course

        val result = repository.getCourseById("id")

        result shouldBeEqualTo course.right()
    }

    @Test
    fun `GIVEN invalid course id WHEN getCourseById THEN return error`() = runTest {
        coEvery { localAudioCoursesDataSource.getAudioCourseById("invalid") } returns null

        val result = repository.getCourseById("invalid")

        result.isLeft() shouldBe true
        result.mapLeft { it.message shouldBe "AudioCourse not found" }
    }

    @Test
    fun `WHEN reset THEN call reset on local`() = runTest {
        coEvery { localAudioCoursesDataSource.reset() } returns Unit

        repository.reset()

        coVerifyOnce { localAudioCoursesDataSource.reset() }
    }
}
