package soy.gabimoreno.domain.repository

import arrow.core.Either
import soy.gabimoreno.data.network.model.Category
import soy.gabimoreno.domain.model.content.Post

interface ContentRepository {

    suspend fun getPosts(categories: List<Category>): Either<Throwable, List<Post>>
}
