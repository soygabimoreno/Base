package soy.gabimoreno.data.network.service

import retrofit2.http.GET

// TODO: To reuse for other purposes
interface PodcastService {

    @GET("74bc02a4/podcast/rss")
    suspend fun getPodcast(): Any
}
