package soy.gabimoreno.data.remote.client

import com.google.common.net.HttpHeaders.AUTHORIZATION
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import soy.gabimoreno.BuildConfig
import soy.gabimoreno.framework.datastore.EMPTY_BEARER_TOKEN

object APIClient {
    var bearerToken = EMPTY_BEARER_TOKEN

    fun createHttpClient(): OkHttpClient {
        val requestInterceptor =
            Interceptor { chain ->
                val request =
                    chain
                        .request()
                        .newBuilder()

                if (bearerToken != EMPTY_BEARER_TOKEN) {
                    request.addHeader(AUTHORIZATION, bearerToken)
                }

                return@Interceptor chain.proceed(request.build())
            }

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =
            when {
                BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.NONE
            }

        val httpClient =
            OkHttpClient
                .Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(httpLoggingInterceptor)
        return httpClient.build()
    }
}
