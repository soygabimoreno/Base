package soy.gabimoreno.data.network.repository

import arrow.core.Either
import soy.gabimoreno.data.network.mapper.toDomain
import soy.gabimoreno.data.network.model.Category
import soy.gabimoreno.data.network.model.toQueryValue
import soy.gabimoreno.data.network.service.ContentService
import soy.gabimoreno.domain.model.content.Post
import soy.gabimoreno.domain.repository.ContentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkContentRepository @Inject constructor(
    private val service: ContentService,
) : ContentRepository {

    override suspend fun getPosts(
        categories: List<Category>,
    ): Either<Throwable, List<Post>> {
        return Either.catch {
            service.getPosts(
                categories.toQueryValue()
            ).toDomain()
        }
    }
}
