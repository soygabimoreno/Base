package soy.gabimoreno.data.remote.repository

import arrow.core.left
import arrow.core.right
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyNever
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.local.audiocourse.LocalAudioCoursesDataSource
import soy.gabimoreno.data.remote.datasource.audiocourses.RemoteAudioCoursesDataSource
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCoursesRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.fake.buildAudioCourse
import soy.gabimoreno.fake.buildAudioCourses

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
            val categories = listOf(Category.AUDIOCOURSES)
            val audioCourses = listOf(relaxedMockk<AudioCourse>())

            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCoursesDataSource.isEmpty() } returns true
            coEvery { localAudioCoursesDataSource.getAudioCoursesWithItems() } returns emptyList()
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns audioCourses.right()
            coEvery { localAudioCoursesDataSource.getAudioCourses() } returns audioCourses

            val result = repository.getCourses(categories)

            result shouldBeEqualTo audioCourses.right()
            coVerifyOnce {
                remoteAudioCoursesDataSource.getAudioCourses(categories)
                localAudioCoursesDataSource.saveAudioCourses(any())
            }
        }

    @Test
    fun `GIVEN local has ARE listened items WHEN getCourses THEN remote items are listened`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { localAudioCoursesDataSource.isEmpty() } returns false
            val localSnapshot = buildAudioCourses(hasBeenListened = true)
            coEvery { localAudioCoursesDataSource.getAudioCoursesWithItems() } returns localSnapshot
            val remoteCourses = buildAudioCourses(hasBeenListened = false)
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
            coEvery { localAudioCoursesDataSource.saveAudioCourses(any()) } just Runs

            repository.getCourses(categories = categories, forceRefresh = false)

            val expectedMerged = remoteCourses.map { course ->
                course.copy(audios = course.audios.map { it.copy(hasBeenListened = true) })
            }
            coVerifyOnce { localAudioCoursesDataSource.saveAudioCourses(expectedMerged) }
        }

    @Test
    fun `GIVEN local has NOT listened items WHEN getCourses THEN remote items remain unlistened`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val localCoursesWithItems = buildAudioCourses()
            val remoteCourses = buildAudioCourses()
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
            coEvery { localAudioCoursesDataSource.isEmpty() } returns false
            coEvery { localAudioCoursesDataSource.getAudioCoursesWithItems() } returns localCoursesWithItems
            coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
            coEvery { localAudioCoursesDataSource.saveAudioCourses(any()) } just runs
            coEvery { localAudioCoursesDataSource.getAudioCourses() } returns remoteCourses

            val result = repository.getCourses(categories)

            val expectedHasBeenListened =
                result.getOrNull()!!.flatMap { it.audios }.none { it.hasBeenListened }
            expectedHasBeenListened shouldBeEqualTo true
            coVerifyOnce {
                localAudioCoursesDataSource.saveAudioCourses(match {
                    it.flatMap { audioCourse -> audioCourse.audios }
                        .none { audioCourseItem -> audioCourseItem.hasBeenListened }
                })
            }
        }

    @Test
    fun `GIVEN local is empty WHEN getCourses THEN fetch from remote`() = runTest {
        val categories = listOf(Category.AUDIOCOURSES)
        val remoteCourses = buildAudioCourses()
        coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
        coEvery { localAudioCoursesDataSource.isEmpty() } returns true
        coEvery { localAudioCoursesDataSource.getAudioCoursesWithItems() } returns emptyList()
        coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns remoteCourses.right()
        coEvery { localAudioCoursesDataSource.getAudioCourses() } returns remoteCourses

        val result = repository.getCourses(categories, forceRefresh = false)

        result shouldBeEqualTo remoteCourses.right()
        coVerifyOnce {
            remoteAudioCoursesDataSource.getAudioCourses(categories)
            localAudioCoursesDataSource.saveAudioCourses(remoteCourses)
        }
    }

    @Test
    fun `GIVEN remote fails WHEN getCourses THEN return error`() = runTest {
        val categories = listOf(Category.AUDIOCOURSES)
        val error = Throwable("Network error")
        coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns true
        coEvery { remoteAudioCoursesDataSource.getAudioCourses(categories) } returns error.left()

        val result = repository.getCourses(listOf(Category.AUDIOCOURSES))

        result shouldBeEqualTo error.left()
    }

    @Test
    fun `GIVEN no refresh and local is not empty WHEN getCourses THEN fetch from local only`() =
        runTest {
            val categories = listOf(Category.AUDIOCOURSES)
            val courses = listOf(relaxedMockk<AudioCourse>())
            coEvery { refreshPremiumAudiosFromRemoteUseCase(any(), any()) } returns false
            coEvery { localAudioCoursesDataSource.isEmpty() } returns false
            coEvery { localAudioCoursesDataSource.getAudioCourses() } returns courses

            val result = repository.getCourses(listOf(Category.AUDIOCOURSES))

            result shouldBeEqualTo courses.right()
            coVerifyNever {
                remoteAudioCoursesDataSource.getAudioCourses(categories)
            }
        }

    @Test
    fun `GIVEN valid course id WHEN getCourseById THEN return course`() = runTest {
        val course = relaxedMockk<AudioCourse>()
        val id = "id"
        coEvery {
            localAudioCoursesDataSource.getAudioCourseById(id)
        } returns flowOf(course)

        val result = repository.getCourseById(id)

        result.isRight() shouldBe true
        result.getOrNull()!!.first() shouldBe course
    }

    @Test
    fun `GIVEN invalid course id WHEN getCourseById THEN return error`() = runTest {
        coEvery {
            localAudioCoursesDataSource.getAudioCourseById("invalid")
        } returns flowOf(null)

        val result = repository.getCourseById("invalid")

        result.isLeft() shouldBe true
        result.mapLeft { it.message shouldBe "AudioCourse not found" }
    }

    @Test
    fun `GIVEN item is listened WHEN markAudioCourseItemAsListened THEN field is updated`() =
        runTest {
            val audioCourse = buildAudioCourse()
            repository.markAudioCourseItemAsListened(audioCourse.id, true)

            coVerifyOnce {
                localAudioCoursesDataSource.updateHasBeenListened(audioCourse.id, true)
            }
        }

    @Test
    fun `GIVEN item is unlistened WHEN markAudioCourseItemAsListened THEN field is updated`() =
        runTest {
            val audioCourse = buildAudioCourse()
            repository.markAudioCourseItemAsListened(audioCourse.id, false)

            coVerifyOnce {
                localAudioCoursesDataSource.updateHasBeenListened(audioCourse.id, false)
            }
        }

    @Test
    fun `WHEN reset THEN call reset on local`() = runTest {
        coEvery { localAudioCoursesDataSource.reset() } returns Unit

        repository.reset()

        coVerifyOnce { localAudioCoursesDataSource.reset() }
    }
}
