package soy.gabimoreno.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.prof.rssparser.Parser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import soy.gabimoreno.data.network.client.APIClient
import soy.gabimoreno.data.network.service.PodcastService
import soy.gabimoreno.data.tracker.DefaultTracker
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.domain.repository.PodcastRepository
import soy.gabimoreno.domain.repository.PodcastRepositoryImpl
import soy.gabimoreno.player.exoplayer.PodcastMediaSource
import soy.gabimoreno.player.service.MediaPlayerServiceConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRSSParser(@ApplicationContext context: Context): Parser = Parser.Builder()
        .context(context)
        .cacheExpirationMillis(ONE_DAY_IN_MILLIS)
        .build()

    @Provides
    fun provideHttpClient(): OkHttpClient = APIClient.createHttpClient()

    @Provides
    @Singleton
    fun providePodcastService(
        client: OkHttpClient
    ): PodcastService = APIClient.createPodcastService(client)

    @Provides
    @Singleton
    fun providePodcastRepository(
        rssParser: Parser
    ): PodcastRepository = PodcastRepositoryImpl(rssParser)

    @Provides
    @Singleton
    fun provideMediaPlayerServiceConnection(
        @ApplicationContext context: Context,
        mediaSource: PodcastMediaSource
    ): MediaPlayerServiceConnection = MediaPlayerServiceConnection(context, mediaSource)

    @Provides
    @Singleton
    fun provideTracker(firebaseAnalytics: FirebaseAnalytics): Tracker = DefaultTracker(firebaseAnalytics)

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)
}

private const val ONE_DAY_IN_MILLIS = 24L * 60L * 60L * 1000L
