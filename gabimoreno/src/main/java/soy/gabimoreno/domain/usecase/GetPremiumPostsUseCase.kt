package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.data.network.model.Category
import soy.gabimoreno.domain.model.content.Post
import soy.gabimoreno.domain.repository.ContentRepository
import javax.inject.Inject

class GetPremiumPostsUseCase @Inject constructor(
    private val repository: ContentRepository
) {

    suspend operator fun invoke(categories: List<Category>): Either<Throwable, List<Post>> {
        return repository.getPosts(categories)
    }
}
