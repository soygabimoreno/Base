package soy.gabimoreno.data.network.client

import com.google.common.net.HttpHeaders.AUTHORIZATION
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import soy.gabimoreno.BuildConfig

object APIClient {

    var bearerToken: String? = null

    fun createHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()

            bearerToken?.let {
                request.addHeader(AUTHORIZATION, it)
            }

            return@Interceptor chain.proceed(request.build())
        }

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = when {
            BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(httpLoggingInterceptor)
        return httpClient.build()
    }
}
