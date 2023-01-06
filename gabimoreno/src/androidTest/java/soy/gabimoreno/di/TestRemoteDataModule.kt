package soy.gabimoreno.di

import android.content.Context
import com.prof.rssparser.Parser
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import soy.gabimoreno.data.remote.client.APIClient
import soy.gabimoreno.data.remote.service.LoginService
import soy.gabimoreno.data.remote.service.PostService
import soy.gabimoreno.di.data.ONE_DAY_IN_MILLIS
import soy.gabimoreno.di.data.RemoteDataModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteDataModule::class]
)
object TestRemoteDataModule {

    @Provides
    fun provideHttpClient(): OkHttpClient = APIClient.createHttpClient()

    @Provides
    @Singleton
    @BaseUrl
    fun provideUrl(): String = TEST_BASE_URL

    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        @BaseUrl baseUrl: String,
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideLoginService(
        retrofit: Retrofit,
    ): LoginService = retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideContentService(
        retrofit: Retrofit,
    ): PostService = retrofit.create(PostService::class.java)

    @Provides
    fun provideRSSParser(@ApplicationContext context: Context): Parser = Parser.Builder()
        .context(context)
        .cacheExpirationMillis(ONE_DAY_IN_MILLIS)
        .build()
}

private const val TEST_BASE_URL = "http://localhost:8080/"
