package soy.gabimoreno.data.network.client

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import soy.gabimoreno.data.network.service.GabiMorenoService

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

    fun createGabiMorenoService(
        client: OkHttpClient
    ): GabiMorenoService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GabiMorenoService::class.java)
    }
}

private const val BASE_URL = "https://gabimoreno.soy/"
