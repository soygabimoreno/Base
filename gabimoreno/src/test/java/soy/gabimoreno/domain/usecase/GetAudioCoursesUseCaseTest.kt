package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.framework.datastore.getEmail

class GetAudioCoursesUseCaseTest {

    private val context = mockk<Context>()

    private val repository = mockk<AudioCoursesRepository>()
    private lateinit var useCase: GetAudioCoursesUseCase

    @Before
    fun setUp() {
        mockkStatic("soy.gabimoreno.framework.datastore.DataStoreEmailKt")
        every { context.getEmail() } returns flowOf(EMAIL)
        useCase = GetAudioCoursesUseCase(repository, context)
    }

    @Test
    fun `GIVEN repository returns Right WHEN invoked THEN returns list of AudioCourse`() = runTest {
        val expectedCourses = listOf(relaxedMockk<AudioCourse>())
        coEvery {
            repository.getCourses(categories = CATEGORIES, email = EMAIL, forceRefresh = false)
        } returns expectedCourses.right()

        val result = useCase(CATEGORIES)

        result shouldBeEqualTo expectedCourses.right()
    }

    @Test
    fun `GIVEN repository returns Left WHEN invoked THEN returns error`() = runTest {
        val error = Throwable("Network error")
        coEvery {
            repository.getCourses(categories = CATEGORIES, email = EMAIL, forceRefresh = false)
        } returns error.left()

        val result = useCase(CATEGORIES)

        result shouldBeEqualTo error.left()
    }
}

private val CATEGORIES = listOf(Category.PREMIUM)
private const val EMAIL = "test@test.com"
