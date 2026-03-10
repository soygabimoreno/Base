package soy.gabimoreno.data.remote.datasource.audiocourses

import arrow.core.Either
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.remote.service.POSTS_PAGE
import soy.gabimoreno.data.remote.service.POSTS_PER_PAGE
import soy.gabimoreno.domain.model.content.AudioCourse

interface AudioCoursesDataSource {
    suspend fun getAudioCourses(
        categories: List<Category>,
        postsPerPage: Int = POSTS_PER_PAGE,
        page: Int = POSTS_PAGE,
    ): Either<Throwable, List<AudioCourse>>
}
