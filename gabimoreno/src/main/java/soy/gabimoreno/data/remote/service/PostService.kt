package soy.gabimoreno.data.remote.service

import retrofit2.http.GET
import retrofit2.http.Query
import soy.gabimoreno.data.remote.model.post.PostApiModel

interface PostService {

    @GET("wp-json/wp/v2/posts/")
    suspend fun getPosts(
        @Query("categories") categoriesQuery: String,
        @Query("per_page") postsPerPage: Int = POSTS_PER_PAGE,
    ): List<PostApiModel>
}

private const val POSTS_PER_PAGE = 200
