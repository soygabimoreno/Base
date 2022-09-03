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
import soy.gabimoreno.data.network.repository.NetworkGabiMorenoRepository
import soy.gabimoreno.data.network.repository.NetworkPodcastRepository
import soy.gabimoreno.data.network.service.GabiMorenoService
import soy.gabimoreno.data.tracker.DefaultTracker
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.domain.repository.GabiMorenoRepository
import soy.gabimoreno.domain.repository.PodcastRepository
import soy.gabimoreno.domain.usecase.GetTrackingEventNameUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase
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
    fun provideGabiMorenoService(
        client: OkHttpClient
    ): GabiMorenoService = APIClient.createGabiMorenoService(client)

    @Provides
    @Singleton
    fun provideGabiMorenoRepository(
        service: GabiMorenoService
    ): GabiMorenoRepository = NetworkGabiMorenoRepository(service)

    @Provides
    @Singleton
    fun providePodcastRepository(
        rssParser: Parser
    ): PodcastRepository = NetworkPodcastRepository(rssParser)

    @Provides
    @Singleton
    fun provideMediaPlayerServiceConnection(
        @ApplicationContext context: Context,
        mediaSource: PodcastMediaSource
    ): MediaPlayerServiceConnection = MediaPlayerServiceConnection(context, mediaSource)

    @Provides
    @Singleton
    fun provideTracker(
        firebaseAnalytics: FirebaseAnalytics,
        getTrackingEventNameUseCase: GetTrackingEventNameUseCase
    ): Tracker = DefaultTracker(
        firebaseAnalytics,
        getTrackingEventNameUseCase
    )

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideSaveCredentialsInDataStoreUseCase(
        @ApplicationContext context: Context
    ): SaveCredentialsInDataStoreUseCase =
        SaveCredentialsInDataStoreUseCase(context)
}

private const val ONE_DAY_IN_MILLIS = 24L * 60L * 60L * 1000L
