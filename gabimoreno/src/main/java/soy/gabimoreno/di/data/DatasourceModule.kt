package soy.gabimoreno.di.data

import com.prof18.rssparser.RssParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.data.remote.datasource.login.RemoteLoginDatasource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.data.remote.datasource.podcast.RemotePodcastDatasource
import soy.gabimoreno.data.remote.datasource.premiumaudios.PremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.senior.DefaultRemoteSeniorDatasource
import soy.gabimoreno.data.remote.datasource.senior.RemoteSeniorDatasource
import soy.gabimoreno.data.remote.service.LoginService
import soy.gabimoreno.data.remote.service.PostService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatasourceModule {
    @Provides
    @Singleton
    fun providePodcastUrl(): PodcastUrl = PODCAST_URL

    @Provides
    @Singleton
    fun provideLoginDatasource(loginService: LoginService): LoginDatasource =
        RemoteLoginDatasource(loginService)

    @Provides
    @Singleton
    fun provideContentDatasource(postService: PostService): PremiumAudiosDataSource =
        RemotePremiumAudiosDataSource(postService)

    @Provides
    @Singleton
    fun providePodcastDatasource(
        rssParser: RssParser,
        okkHttpClient: okhttp3.OkHttpClient,
    ): PodcastDatasource =
        RemotePodcastDatasource(
            rssParser,
            okkHttpClient,
        )

    @Provides
    @Singleton
    fun provideRemoteSeniorDatasource(
        rssParser: RssParser,
        okkHttpClient: okhttp3.OkHttpClient,
    ): RemoteSeniorDatasource =
        DefaultRemoteSeniorDatasource(
            rssParser,
            okkHttpClient,
        )
}

typealias PodcastUrl = String

private const val PODCAST_URL = "https://anchor.fm/s/74bc02a4/podcast/rss"
