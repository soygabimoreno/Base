package soy.gabimoreno.domain.usecase

import arrow.core.left
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

class GetAudioCourseItemByIdUseCaseTest {
    private val repository = mockk<AudioCoursesRepository>()
    private lateinit var useCase: GetAudioCourseItemByIdUseCase

    @Before
    fun setUp() {
        useCase = GetAudioCourseItemByIdUseCase(repository)
    }

    @Test
    fun `GIVEN repository returns Right WHEN invoked THEN returns AudioCourseItem`() =
        runTest {
            val audioCourseItem = buildAudioCourseItem()
            coEvery {
                repository.getAudioCourseItem(audioCourseItem.id)
            } returns right(audioCourseItem)

            val result = useCase(audioCourseItem.id)

            result.isRight() shouldBe true
            result.getOrNull() shouldBe audioCourseItem
        }

    @Test
    fun `GIVEN repository returns Left WHEN invoked THEN returns error`() =
        runTest {
            val audioCourseItem = buildAudioCourseItem()
            val error = Throwable("Network error")
            coEvery {
                repository.getAudioCourseItem(audioCourseItem.id)
            } returns left(error)

            val result = useCase(audioCourseItem.id)

            result shouldBeEqualTo error.left()
        }
}
