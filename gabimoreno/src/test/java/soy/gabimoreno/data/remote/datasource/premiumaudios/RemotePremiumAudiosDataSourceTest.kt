package soy.gabimoreno.data.remote.datasource.premiumaudios

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.remote.model.post.PostApiModel
import soy.gabimoreno.data.remote.model.toQueryValue
import soy.gabimoreno.data.remote.service.PostService

@ExperimentalCoroutinesApi
class RemotePremiumAudiosDataSourceTest {

    private val postService: PostService = mockk()
    private lateinit var remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource

    @Before
    fun setUp() {
        remotePremiumAudiosDataSource = RemotePremiumAudiosDataSource(
            postService
        )
    }

    @Test
    fun `GIVEN a success posts WHEN getPosts THEN get the expected result`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val categoriesQuery = categories.toQueryValue()
            val apiModelList: List<PostApiModel> = relaxedMockk()
            coEvery { postService.getPosts(categoriesQuery) } returns apiModelList

            val result = remotePremiumAudiosDataSource.getPremiumAudios(categories)

            result.isRight().shouldBeTrue()
            coVerifyOnce { postService.getPosts(categoriesQuery) }
        }

    @Test
    fun `GIVEN a failure posts WHEN getPosts THEN get the expected error`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val categoriesQuery = categories.toQueryValue()
            coEvery { postService.getPosts(categoriesQuery) } throws Throwable()

            val result = remotePremiumAudiosDataSource.getPremiumAudios(categories)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { postService.getPosts(categoriesQuery) }
        }
}
