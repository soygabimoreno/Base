package soy.gabimoreno.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.domain.usecase.EncodeUrlUseCase
import soy.gabimoreno.domain.usecase.GetAppVersionNameUseCase
import soy.gabimoreno.domain.usecase.GetAudioByIdUseCase
import soy.gabimoreno.domain.usecase.GetPodcastStreamUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReversePodcastOrderUseCase
import soy.gabimoreno.domain.usecase.HomeUseCases
import soy.gabimoreno.domain.usecase.MarkPodcastAsListenedUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReversePodcastOrderUseCase
import soy.gabimoreno.domain.usecase.UpdateAudioItemFavoriteStateUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {
    @Provides
    @Singleton
    fun provideHomeUseCases(
        getPodcastStreamUseCase: GetPodcastStreamUseCase,
        markPodcastAsListenedUseCase: MarkPodcastAsListenedUseCase,
        updateAudioItemFavoriteStateUseCase: UpdateAudioItemFavoriteStateUseCase,
        encodeUrlUseCase: EncodeUrlUseCase,
        getAppVersionNameUseCase: GetAppVersionNameUseCase,
        getShouldIReversePodcastOrderUseCase: GetShouldIReversePodcastOrderUseCase,
        setShouldIReversePodcastOrderUseCase: SetShouldIReversePodcastOrderUseCase,
    ) = HomeUseCases(
        getPodcastStreamUseCase = getPodcastStreamUseCase,
        markPodcastAsListenedUseCase = markPodcastAsListenedUseCase,
        updateAudioItemFavoriteStateUseCase = updateAudioItemFavoriteStateUseCase,
        encodeUrl = encodeUrlUseCase,
        getAppVersionName = getAppVersionNameUseCase,
        getShouldIReversePodcastOrder = getShouldIReversePodcastOrderUseCase,
        setShouldIReversePodcastOrder = setShouldIReversePodcastOrderUseCase,
    )

    @Provides
    @Singleton
    fun provideGetAudioByIdUseCase(
        audioCoursesRepository: AudioCoursesRepository,
        podcastRepository: PodcastRepository,
        premiumAudiosRepository: PremiumAudiosRepository,
    ) = GetAudioByIdUseCase(
        audioCoursesRepository = audioCoursesRepository,
        podcastRepository = podcastRepository,
        premiumAudiosRepository = premiumAudiosRepository,
    )
}
