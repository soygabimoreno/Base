package soy.gabimoreno.data.network.mapper

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.data.network.model.PostApiModel

class PostMapperKtTest {

    @Test
    fun `GIVEN a PostApiModel list WHEN toDomain THEN return the expected posts`() {
        val postApiModelList = listOf(buildPostApiModel())

        val result = postApiModelList.toDomain()

        result.forEach { post ->
            post.id shouldBeEqualTo ID
        }
    }

    private fun buildPostApiModel() = PostApiModel(
        id = ID
    )
}

private const val ID = 1234L
