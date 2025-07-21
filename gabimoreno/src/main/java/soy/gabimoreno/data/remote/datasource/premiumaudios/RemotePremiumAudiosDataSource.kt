package soy.gabimoreno.data.remote.datasource.premiumaudios

import arrow.core.Either
import soy.gabimoreno.data.remote.mapper.toDomain
import soy.gabimoreno.data.remote.mapper.toPremiumAudios
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.remote.model.toQueryValue
import soy.gabimoreno.data.remote.service.PostService
import soy.gabimoreno.domain.model.content.PremiumAudio
import javax.inject.Inject

class RemotePremiumAudiosDataSource
    @Inject
    constructor(
        private val postService: PostService,
    ) : PremiumAudiosDataSource {
        override suspend fun getPremiumAudios(
            categories: List<Category>,
            postsPerPage: Int,
            page: Int,
        ): Either<Throwable, List<PremiumAudio>> =
            Either.catch {
                postService
                    .getPosts(
                        categories.toQueryValue(),
                        postsPerPage = postsPerPage,
                        page = page,
                    ).toDomain()
                    .toPremiumAudios()
            }
    }
