package soy.gabimoreno.data.remote.datasource.audiocourse

import arrow.core.Either
import soy.gabimoreno.data.remote.mapper.toDomain
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.remote.model.toQueryValue
import soy.gabimoreno.data.remote.service.PostService
import soy.gabimoreno.domain.model.content.AudioCourse
import javax.inject.Inject

class RemoteAudioCourseDataSource
    @Inject
    constructor(
        private val postService: PostService,
    ) : AudioCourseDataSource {
        override suspend fun getAudioCourses(
            categories: List<Category>,
            postsPerPage: Int,
            page: Int,
        ): Either<Throwable, List<AudioCourse>> =
            Either.catch {
                postService
                    .getAudioCourses(
                        categoriesQuery = categories.toQueryValue(),
                        postsPerPage = postsPerPage,
                        page = page,
                    ).toDomain()
            }
    }
