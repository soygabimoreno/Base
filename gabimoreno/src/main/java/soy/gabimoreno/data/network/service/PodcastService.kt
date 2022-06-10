package soy.gabimoreno.data.network.service

import retrofit2.http.GET
import soy.gabimoreno.data.network.model.RssApiModel

interface PodcastService {

    @GET("74bc02a4/podcast/rss")
    suspend fun getPodcast(): RssApiModel
}
