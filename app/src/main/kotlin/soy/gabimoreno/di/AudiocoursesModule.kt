package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.audiocourse.AudioCourseRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.premiumaudio.PremiumAudiosRepository
import soy.gabimoreno.domain.usecase.GetAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.MarkAudioCourseItemAsListenedUseCase
import soy.gabimoreno.domain.usecase.SetAllAudiocoursesAsUnlistenedUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.UpdateAudioItemFavoriteStateUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudiocoursesModule {
    @Provides
    @Singleton
    fun provideGetShouldIReloadAudioCoursesUseCase(
        @ApplicationContext context: Context,
    ) = GetShouldIReloadAudioCoursesUseCase(context)

    @Provides
    @Singleton
    fun provideSetShouldIReloadAudioCoursesUseCase(
        @ApplicationContext context: Context,
    ) = SetShouldIReloadAudioCoursesUseCase(context)

    @Provides
    @Singleton
    fun provideGetAudioCoursesUseCase(
        audioCourseRepository: AudioCourseRepository,
        @ApplicationContext context: Context,
    ): GetAudioCoursesUseCase =
        GetAudioCoursesUseCase(context = context, audioCourseRepository = audioCourseRepository)

    @Provides
    @Singleton
    fun provideUpdateAudioItemFavoriteStateUseCase(
        audioCourseRepository: AudioCourseRepository,
        premiumAudiosRepository: PremiumAudiosRepository,
        podcastRepository: PodcastRepository,
        @ApplicationContext context: Context,
    ): UpdateAudioItemFavoriteStateUseCase =
        UpdateAudioItemFavoriteStateUseCase(
            audioCourseRepository = audioCourseRepository,
            context = context,
            premiumAudioCoursesRepository = premiumAudiosRepository,
            podcastRepository = podcastRepository,
        )

    @Provides
    @Singleton
    fun provideMarkAudioCourseItemAsListenedUseCase(
        audioCourseRepository: AudioCourseRepository,
        @ApplicationContext context: Context,
    ): MarkAudioCourseItemAsListenedUseCase =
        MarkAudioCourseItemAsListenedUseCase(
            context = context,
            audioCourseRepository = audioCourseRepository,
        )

    @Provides
    @Singleton
    fun provideSetAllAudiocoursesAsUnlistenedUseCase(
        audioCourseRepository: AudioCourseRepository,
        @ApplicationContext context: Context,
    ): SetAllAudiocoursesAsUnlistenedUseCase =
        SetAllAudiocoursesAsUnlistenedUseCase(
            context = context,
            audioCourseRepository = audioCourseRepository,
        )
}
