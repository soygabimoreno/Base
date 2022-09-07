package soy.gabimoreno.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.data.tracker.DefaultTracker
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.domain.usecase.GetTrackingEventNameUseCase
import soy.gabimoreno.player.exoplayer.PodcastMediaSource
import soy.gabimoreno.player.service.MediaPlayerServiceConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMediaPlayerServiceConnection(
        @ApplicationContext context: Context,
        mediaSource: PodcastMediaSource,
    ): MediaPlayerServiceConnection = MediaPlayerServiceConnection(context, mediaSource)

    @Provides
    @Singleton
    fun provideTracker(
        firebaseAnalytics: FirebaseAnalytics,
        getTrackingEventNameUseCase: GetTrackingEventNameUseCase,
    ): Tracker = DefaultTracker(
        firebaseAnalytics,
        getTrackingEventNameUseCase
    )

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)
}
