package soy.gabimoreno.data.network.client

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import soy.gabimoreno.data.network.service.PodcastService

object APIClient {

    fun createHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .build()

            return@Interceptor chain.proceed(request)
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)

        return httpClient.build()
    }

    fun createPodcastService(
        client: OkHttpClient
    ): PodcastService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
//            .addConverterFactory(ParseRSSConverterFactory.create<RSSFeedObject>())
            .build()
            .create(PodcastService::class.java)
    }
}


private const val BASE_URL = "https://anchor.fm/s/"
