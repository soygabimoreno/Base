package soy.gabimoreno.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query
import soy.gabimoreno.data.network.model.post.PostApiModel

interface ContentService {

    @GET("wp-json/wp/v2/posts/")
    suspend fun getPosts(
        @Query("categories") categoriesQuery: String,
        @Query("per_page") postsPerPage: Int = POSTS_PER_PAGE,
    ): List<PostApiModel>
}

private const val POSTS_PER_PAGE = 100
