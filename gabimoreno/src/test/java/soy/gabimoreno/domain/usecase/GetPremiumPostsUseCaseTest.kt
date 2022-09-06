package soy.gabimoreno.domain.usecase

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test
import soy.gabimoreno.core.testing.coVerifyOnce
import soy.gabimoreno.data.network.model.Category
import soy.gabimoreno.data.network.repository.NetworkContentRepository
import soy.gabimoreno.fake.buildPosts

@ExperimentalCoroutinesApi
class GetPremiumPostsUseCaseTest {

    private val repository: NetworkContentRepository = mockk()
    private lateinit var useCase: GetPremiumPostsUseCase

    @Before
    fun setUp() {
        useCase = GetPremiumPostsUseCase(
            repository
        )
    }

    @Test
    fun `GIVEN the happy path WHEN invoke THEN get the expected result`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val posts = buildPosts()
            coEvery { repository.getPosts(categories) } returns posts.right()

            val result = useCase(categories)

            result.isRight().shouldBeTrue()
            coVerifyOnce { repository.getPosts(categories) }
        }

    @Test
    fun `GIVEN a failure posts WHEN invoke THEN get the expected error`() =
        runTest {
            val categories = listOf(Category.PREMIUM_ALGORITHMS, Category.PREMIUM_AUDIO_COURSES)
            val throwable = Throwable()
            coEvery { repository.getPosts(categories) } returns throwable.left()

            val result = useCase(categories)

            result.isLeft().shouldBeTrue()
            coVerifyOnce { repository.getPosts(categories) }
        }
}
