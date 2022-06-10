package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import soy.gabimoreno.data.datastore.PodcastDataStore
import soy.gabimoreno.data.network.client.APIClient
import soy.gabimoreno.data.network.service.PodcastService
import soy.gabimoreno.domain.repository.PodcastRepository
import soy.gabimoreno.domain.repository.PodcastRepositoryImpl
import soy.gabimoreno.player.exoplayer.PodcastMediaSource
import soy.gabimoreno.player.service.MediaPlayerServiceConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideHttpClient(): OkHttpClient = APIClient.createHttpClient()

    @Provides
    @Singleton
    fun providePodcastService(
        client: OkHttpClient
    ): PodcastService = APIClient.createPodcastService(client)

    @Provides
    @Singleton
    fun providePodcastDataStore(
        @ApplicationContext context: Context
    ): PodcastDataStore = PodcastDataStore(context)

    @Provides
    @Singleton
    fun providePodcastRepository(
        service: PodcastService,
        dataStore: PodcastDataStore
    ): PodcastRepository = PodcastRepositoryImpl(service, dataStore)

    @Provides
    @Singleton
    fun provideMediaPlayerServiceConnection(
        @ApplicationContext context: Context,
        mediaSource: PodcastMediaSource
    ): MediaPlayerServiceConnection = MediaPlayerServiceConnection(context, mediaSource)
}
