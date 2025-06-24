package soy.gabimoreno.data.remote.service

import retrofit2.http.GET
import retrofit2.http.Query
import soy.gabimoreno.data.remote.model.course.CourseApiModel
import soy.gabimoreno.data.remote.model.post.PostApiModel

interface PostService {

    @GET("wp-json/wp/v2/posts/")
    suspend fun getPosts(
        @Query("categories") categoriesQuery: String,
        @Query("_fields") fieldsQuery: String = FIELDS,
        @Query("per_page") postsPerPage: Int = POSTS_PER_PAGE,
        @Query("page") page: Int = POSTS_PAGE
    ): List<PostApiModel>

    @GET("wp-json/wp/v2/posts/")
    suspend fun getAudioCourses(
        @Query("categories") categoriesQuery: String,
        @Query("per_page") postsPerPage: Int = POSTS_PER_PAGE,
        @Query("page") page: Int = POSTS_PAGE,
    ): List<CourseApiModel>
}

internal const val POSTS_PER_PAGE = 20
internal const val POSTS_PAGE = 1
private const val FIELDS = "id,title,content,excerpt,author,categories,link,date"
