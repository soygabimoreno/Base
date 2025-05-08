package soy.gabimoreno.data.remote.datasource.audiocourses

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.data.remote.service.PostService
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.remote.model.course.CourseApiModel
import soy.gabimoreno.data.remote.model.toQueryValue
import soy.gabimoreno.core.testing.relaxedMockk


class RemoteAudioCoursesDataSourceTest {

    private val postService = mockk<PostService>()
    private lateinit var remoteAudioCoursesDataSource: RemoteAudioCoursesDataSource

    @Before
    fun setUp() {
        remoteAudioCoursesDataSource = RemoteAudioCoursesDataSource(postService)
    }

    @Test
    fun `GIVEN audioCourses WHEN getAudioCourses THEN get the expected result`() = runTest {
        val categories = listOf(Category.AUDIOCOURSES)
        val categoriesQuery = categories.toQueryValue()
        val apiModelList: List<CourseApiModel> = relaxedMockk()
        coEvery { postService.getAudioCourses(categoriesQuery) } returns apiModelList

        val result = remoteAudioCoursesDataSource.getAudioCourses(categories)

        result.isRight() shouldBe true
        coVerifyOnce { postService.getAudioCourses(categoriesQuery) }
    }

    @Test
    fun `GIVEN a failure audioCourses WHEN audioCourses THEN get the expected error`() = runTest {
        val categories = listOf(Category.AUDIOCOURSES)
        val categoriesQuery = categories.toQueryValue()
        coEvery { postService.getAudioCourses(categoriesQuery) } throws Throwable()

        val result = remoteAudioCoursesDataSource.getAudioCourses(categories)

        result.isLeft() shouldBe true
        coVerifyOnce { postService.getAudioCourses(categoriesQuery) }
    }
}
