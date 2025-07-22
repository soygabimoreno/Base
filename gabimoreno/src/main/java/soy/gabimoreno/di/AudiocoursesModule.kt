package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
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
        audioCoursesRepository: AudioCoursesRepository,
        @ApplicationContext context: Context,
    ): GetAudioCoursesUseCase =
        GetAudioCoursesUseCase(context = context, audioCoursesRepository = audioCoursesRepository)

    @Provides
    @Singleton
    fun provideUpdateAudioItemFavoriteStateUseCase(
        audioCoursesRepository: AudioCoursesRepository,
        premiumAudiosRepository: PremiumAudiosRepository,
        @ApplicationContext context: Context,
    ): UpdateAudioItemFavoriteStateUseCase =
        UpdateAudioItemFavoriteStateUseCase(
            audioCoursesRepository = audioCoursesRepository,
            context = context,
            premiumAudioCoursesRepository = premiumAudiosRepository,
        )

    @Provides
    @Singleton
    fun provideMarkAudioCourseItemAsListenedUseCase(
        audioCoursesRepository: AudioCoursesRepository,
        @ApplicationContext context: Context,
    ): MarkAudioCourseItemAsListenedUseCase =
        MarkAudioCourseItemAsListenedUseCase(
            context = context,
            audioCoursesRepository = audioCoursesRepository,
        )

    @Provides
    @Singleton
    fun provideSetAllAudiocoursesAsUnlistenedUseCase(
        audioCoursesRepository: AudioCoursesRepository,
        @ApplicationContext context: Context,
    ): SetAllAudiocoursesAsUnlistenedUseCase =
        SetAllAudiocoursesAsUnlistenedUseCase(
            context = context,
            audioCoursesRepository = audioCoursesRepository,
        )
}
