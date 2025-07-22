package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.domain.usecase.GetLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudiosManagedUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.domain.usecase.SetAllPremiumAudiosAsUnlistenedUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PremiumAudiosModule {
    @Provides
    @Singleton
    fun provideSaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(
        @ApplicationContext context: Context,
    ) = SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(context)

    @Provides
    @Singleton
    fun provideGetLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(
        @ApplicationContext context: Context,
    ) = GetLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase(context)

    @Provides
    @Singleton
    fun provideMarkPremiumAudioAsListenedUseCase(
        premiumAudiosRepository: PremiumAudiosRepository,
        @ApplicationContext context: Context,
    ): MarkPremiumAudioAsListenedUseCase =
        MarkPremiumAudioAsListenedUseCase(
            context = context,
            premiumAudiosRepository = premiumAudiosRepository,
        )

    @Provides
    @Singleton
    fun provideSetAllPremiumAudiosAsUnlistenedUseCase(
        premiumAudiosRepository: PremiumAudiosRepository,
        @ApplicationContext context: Context,
    ): SetAllPremiumAudiosAsUnlistenedUseCase =
        SetAllPremiumAudiosAsUnlistenedUseCase(
            context = context,
            premiumAudiosRepository = premiumAudiosRepository,
        )

    @Provides
    @Singleton
    fun provideGetPremiumAudiosManagedUseCase(
        @ApplicationContext context: Context,
        premiumAudiosRepository: PremiumAudiosRepository,
    ): GetPremiumAudiosManagedUseCase =
        GetPremiumAudiosManagedUseCase(
            context = context,
            premiumAudiosRepository = premiumAudiosRepository,
        )
}
