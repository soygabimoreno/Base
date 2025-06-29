package soy.gabimoreno.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.ext.left
import soy.gabimoreno.ext.right
import soy.gabimoreno.fake.buildAudioCourseItem

class GetFavoritesAudioItemsUseCaseTest {

    private val repository = mockk<AudioCoursesRepository>()
    private lateinit var useCase: GetFavoritesAudioItemsUseCase

    @Before
    fun setUp() {
        useCase = GetFavoritesAudioItemsUseCase(repository)
    }

    @Test
    fun `GIVEN repository returns Right WHEN invoked THEN returns list of AudioCourseItem`() =
        runTest {
            val items = listOf(
                buildAudioCourseItem(markedAsFavorite = true),
                buildAudioCourseItem(markedAsFavorite = true)
            )
            coEvery { repository.getAllFavoriteAudioCoursesItems() } returns right(items)

            val result = useCase()

            result.isRight() shouldBe true
            result.getOrNull() shouldBe items
        }

    @Test
    fun `GIVEN repository returns Left WHEN invoked THEN returns error`() = runTest {
        val error = Throwable("Database failure")
        coEvery { repository.getAllFavoriteAudioCoursesItems() } returns left(error)

        val result = useCase()

        result shouldBeEqualTo left(error)
    }
}
