package soy.gabimoreno.domain.usecase

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.fake.buildAudioCourse
import soy.gabimoreno.framework.datastore.getEmail

class MarkAudioCourseItemAsListenedUseCaseTest {
    private val context: Context = mockk()
    private val repository = relaxedMockk<AudioCoursesRepository>()

    private lateinit var useCase: MarkAudioCourseItemAsListenedUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = MarkAudioCourseItemAsListenedUseCase(repository, context)
    }

    @Test
    fun `GIVEN item is listened WHEN invoke THEN repository is called`() =
        runTest {
            val audioCourse = buildAudioCourse()
            useCase(audioCourse.id, true)

            coVerifyOnce {
                repository.markAudioCourseItemAsListened(audioCourse.id, EMAIL, true)
            }
        }

    @Test
    fun `GIVEN item is unlistened WHEN invoke THEN repository is called`() =
        runTest {
            val audioCourse = buildAudioCourse()
            useCase(audioCourse.id, false)

            coVerifyOnce {
                repository.markAudioCourseItemAsListened(audioCourse.id, EMAIL, false)
            }
        }
}

private const val EMAIL = "test@test.com"
