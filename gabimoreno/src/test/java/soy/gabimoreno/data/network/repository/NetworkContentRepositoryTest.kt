package soy.gabimoreno.data.network.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.core.testing.relaxedMockk
import soy.gabimoreno.data.network.model.Category
import soy.gabimoreno.data.network.model.post.PostApiModel
import soy.gabimoreno.data.network.model.toQueryValue
import soy.gabimoreno.data.network.service.ContentService

@ExperimentalCoroutinesApi
class NetworkContentRepositoryTest {

    private val service: ContentService = mockk()
    private lateinit var repository: NetworkContentRepository

    @Before
    fun setUp() {
        repository = NetworkContentRepository(
            service
        )
    }

    @Test
    fun `GIVEN a success posts WHEN getPosts THEN get the expected result`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val categoriesQuery = categories.toQueryValue()
            val apiModelList: List<PostApiModel> = relaxedMockk()
            coEvery { service.getPosts(categoriesQuery) } returns apiModelList

            val result = repository.getPosts(categories)

            result.isRight().shouldBeTrue()
            coVerifyOnce { service.getPosts(categoriesQuery) }
        }

    @Test
    fun `GIVEN a failure posts WHEN getPosts THEN get the expected error`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val categoriesQuery = categories.toQueryValue()
            val throwable = Throwable()
            coEvery { service.getPosts(categoriesQuery) } throws throwable

            val result = repository.getPosts(categories)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { service.getPosts(categoriesQuery) }
        }
}
