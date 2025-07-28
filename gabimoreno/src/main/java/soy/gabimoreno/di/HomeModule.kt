package soy.gabimoreno.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.usecase.EncodeUrlUseCase
import soy.gabimoreno.domain.usecase.GetAppVersionNameUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReversePodcastOrderUseCase
import soy.gabimoreno.domain.usecase.HomeUseCases
import soy.gabimoreno.domain.usecase.SetShouldIReversePodcastOrderUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {
    @Provides
    @Singleton
    fun provideHomeUseCases(
        getAppVersionNameUseCase: GetAppVersionNameUseCase,
        encodeUrlUseCase: EncodeUrlUseCase,
        getShouldIReversePodcastOrderUseCase: GetShouldIReversePodcastOrderUseCase,
        setShouldIReversePodcastOrderUseCase: SetShouldIReversePodcastOrderUseCase,
    ) = HomeUseCases(
        getAppVersionName = getAppVersionNameUseCase,
        encodeUrl = encodeUrlUseCase,
        getShouldIReversePodcastOrder = getShouldIReversePodcastOrderUseCase,
        setShouldIReversePodcastOrder = setShouldIReversePodcastOrderUseCase,
    )
}
