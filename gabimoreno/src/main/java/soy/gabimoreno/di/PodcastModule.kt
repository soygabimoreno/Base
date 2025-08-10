package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.usecase.GetPodcastByIdUseCase
import soy.gabimoreno.domain.usecase.GetPodcastStreamUseCase
import soy.gabimoreno.domain.usecase.MarkPodcastAsListenedUseCase
import soy.gabimoreno.domain.usecase.SetAllPodcastAsUnlistenedUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PodcastModule {
    @Provides
    @Singleton
    fun provideGetPodcastStreamUseCase(
        podcastRepository: PodcastRepository,
        @ApplicationContext context: Context,
    ) = GetPodcastStreamUseCase(
        context = context,
        podcastRepository = podcastRepository,
    )

    @Provides
    @Singleton
    fun provideMarkPodcastAsListenedUseCase(
        podcastRepository: PodcastRepository,
        @ApplicationContext context: Context,
    ) = MarkPodcastAsListenedUseCase(
        context = context,
        podcastRepository = podcastRepository,
    )

    @Provides
    @Singleton
    fun provideSetAllPodcastAsUnlistenedUseCase(
        podcastRepository: PodcastRepository,
        @ApplicationContext context: Context,
    ) = SetAllPodcastAsUnlistenedUseCase(
        context = context,
        podcastRepository = podcastRepository,
    )

    @Provides
    @Singleton
    fun provideGetPodcastByIdUseCase(podcastRepository: PodcastRepository) =
        GetPodcastByIdUseCase(
            podcastRepository = podcastRepository,
        )
}
