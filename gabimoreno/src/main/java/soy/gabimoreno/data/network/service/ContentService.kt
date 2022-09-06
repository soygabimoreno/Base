package soy.gabimoreno.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query
import soy.gabimoreno.data.network.model.PostApiModel

interface ContentService {

    @GET("wp-json/wp/v2/posts/")
    suspend fun getPosts(
        @Query("categories") categories: String
    ): List<PostApiModel>
}
