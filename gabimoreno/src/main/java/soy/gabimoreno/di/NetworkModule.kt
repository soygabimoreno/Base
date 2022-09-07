package soy.gabimoreno.di

import android.content.Context
import com.prof.rssparser.Parser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import soy.gabimoreno.data.network.client.APIClient
import soy.gabimoreno.data.network.repository.NetworkContentRepository
import soy.gabimoreno.data.network.repository.NetworkLoginRepository
import soy.gabimoreno.data.network.repository.NetworkPodcastRepository
import soy.gabimoreno.data.network.service.ContentService
import soy.gabimoreno.data.network.service.LoginService
import soy.gabimoreno.domain.repository.ContentRepository
import soy.gabimoreno.domain.repository.LoginRepository
import soy.gabimoreno.domain.repository.PodcastRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRSSParser(@ApplicationContext context: Context): Parser = Parser.Builder()
        .context(context)
        .cacheExpirationMillis(ONE_DAY_IN_MILLIS)
        .build()

    @Provides
    fun provideHttpClient(): OkHttpClient = APIClient.createHttpClient()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
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
    ): ContentService = retrofit.create(ContentService::class.java)

    @Provides
    @Singleton
    fun provideLoginRepository(
        service: LoginService,
    ): LoginRepository = NetworkLoginRepository(service)

    @Provides
    @Singleton
    fun provideContentRepository(
        service: ContentService,
    ): ContentRepository = NetworkContentRepository(service)

    @Provides
    @Singleton
    fun providePodcastRepository(
        rssParser: Parser,
    ): PodcastRepository = NetworkPodcastRepository(rssParser)
}

private const val ONE_DAY_IN_MILLIS = 24L * 60L * 60L * 1000L
private const val BASE_URL = "https://gabimoreno.soy/"
