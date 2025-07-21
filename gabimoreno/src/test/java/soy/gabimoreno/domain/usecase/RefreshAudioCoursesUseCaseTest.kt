package soy.gabimoreno.domain.usecase

import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCoursesRepository

class RefreshAudioCoursesUseCaseTest {
    private val repository = mockk<DefaultAudioCoursesRepository>(relaxed = true)

    private lateinit var useCase: RefreshAudioCoursesUseCase

    @Before
    fun setUp() {
        useCase = RefreshAudioCoursesUseCase(repository)
    }

    @Test
    fun `WHEN invoked THEN calls repository reset`() =
        runTest {
            useCase()

            coVerifyOnce { repository.reset() }
        }
}
