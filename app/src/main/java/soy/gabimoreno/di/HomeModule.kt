package soy.gabimoreno.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.audiocourse.AudioCourseRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.premiumaudio.PremiumAudiosRepository
import soy.gabimoreno.domain.repository.senioraudio.SeniorAudioRepository
import soy.gabimoreno.domain.usecase.EncodeUrlUseCase
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
        getShouldIReversePodcastOrderUseCase: GetShouldIReversePodcastOrderUseCase,
        setShouldIReversePodcastOrderUseCase: SetShouldIReversePodcastOrderUseCase,
    ) = HomeUseCases(
        getPodcastStreamUseCase = getPodcastStreamUseCase,
        markPodcastAsListenedUseCase = markPodcastAsListenedUseCase,
        updateAudioItemFavoriteStateUseCase = updateAudioItemFavoriteStateUseCase,
        encodeUrl = encodeUrlUseCase,
        getShouldIReversePodcastOrder = getShouldIReversePodcastOrderUseCase,
        setShouldIReversePodcastOrder = setShouldIReversePodcastOrderUseCase,
    )

    @Provides
    @Singleton
    fun provideGetAudioByIdUseCase(
        audioCourseRepository: AudioCourseRepository,
        podcastRepository: PodcastRepository,
        seniorAudioRepository: SeniorAudioRepository,
        premiumAudiosRepository: PremiumAudiosRepository,
    ) = GetAudioByIdUseCase(
        audioCourseRepository = audioCourseRepository,
        podcastRepository = podcastRepository,
        seniorAudioRepository = seniorAudioRepository,
        premiumAudiosRepository = premiumAudiosRepository,
    )
}
