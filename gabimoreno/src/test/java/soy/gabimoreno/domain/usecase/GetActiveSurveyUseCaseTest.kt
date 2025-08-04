package soy.gabimoreno.domain.usecase

import arrow.core.Either
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.domain.repository.survey.SurveyRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildSurvey

class GetActiveSurveyUseCaseTest {
    private val getLastSurveyIdUseCase: GetLastSurveyIdUseCase = mockk()
    private val repository: SurveyRepository = mockk()

    private lateinit var useCase: GetActiveSurveyUseCase

    @Before
    fun setUp() {
        useCase = GetActiveSurveyUseCase(getLastSurveyIdUseCase, repository)
    }

    @Test
    fun `GIVEN new active survey WHEN invoked THEN returns survey`() =
        runTest {
            val previousId = 0
            val newSurvey = buildSurvey()
            coEvery { getLastSurveyIdUseCase() } returns flowOf(previousId)
            coEvery { repository.getLastedSurvey() } returns right(newSurvey)

            val result = useCase()

            result shouldBeEqualTo right(newSurvey)
        }

    @Test
    fun `GIVEN same survey id WHEN invoked THEN returns Left`() =
        runTest {
            val previousId = 1
            val newSurvey = buildSurvey()
            coEvery { getLastSurveyIdUseCase() } returns flowOf(previousId)
            coEvery { repository.getLastedSurvey() } returns right(newSurvey)

            val result = useCase()

            result shouldBeInstanceOf Either.Left::class
        }

    @Test
    fun `GIVEN inactive survey WHEN invoked THEN returns Left`() =
        runTest {
            val previousId = 0
            val newSurvey = buildSurvey(isActive = false)
            coEvery { getLastSurveyIdUseCase() } returns flowOf(previousId)
            coEvery { repository.getLastedSurvey() } returns right(newSurvey)

            val result = useCase()

            result shouldBeInstanceOf Either.Left::class
        }

    @Test
    fun `GIVEN null survey WHEN invoked THEN returns Left`() =
        runTest {
            val previousId = 0
            coEvery { getLastSurveyIdUseCase() } returns flowOf(previousId)
            coEvery { repository.getLastedSurvey() } returns right(null)

            val result = useCase()

            result shouldBeInstanceOf Either.Left::class
        }

    @Test
    fun `GIVEN repository returns Left WHEN invoked THEN returns Left`() =
        runTest {
            val previousId = 0
            val error = RuntimeException()
            coEvery { getLastSurveyIdUseCase() } returns flowOf(previousId)
            coEvery { repository.getLastedSurvey() } returns left(error)

            val result = useCase()

            result shouldBeInstanceOf Either.Left::class
        }
}
