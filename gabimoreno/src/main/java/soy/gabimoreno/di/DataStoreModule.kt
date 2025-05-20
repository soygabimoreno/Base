package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.session.MemberSession
import soy.gabimoreno.domain.usecase.GetLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReversePodcastOrderUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.IsBearerTokenValid
import soy.gabimoreno.domain.usecase.ResetJwtAuthTokenUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.domain.usecase.SetJwtAuthTokenUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReversePodcastOrderUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.framework.datastore.DataStoreMemberSession
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideSaveCredentialsInDataStoreUseCase(
        @ApplicationContext context: Context,
    ) = SaveCredentialsInDataStoreUseCase(context)

    @Provides
    @Singleton
    fun provideDataStoreMemberSession(
        @ApplicationContext context: Context,
    ): MemberSession = DataStoreMemberSession(context)

    @Provides
    @Singleton
    fun provideIsBearerTokenValid(
        @ApplicationContext context: Context,
    ) = IsBearerTokenValid(context)

    @Provides
    @Singleton
    fun provideSetJwtAuthTokenUseCase(
        @ApplicationContext context: Context,
    ) = SetJwtAuthTokenUseCase(context)

    @Provides
    @Singleton
    fun provideResetJwtAuthTokenUseCase(
        @ApplicationContext context: Context,
    ) = ResetJwtAuthTokenUseCase(context)

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
    fun provideGetShouldIInvestPodcastOrderUseCase(
        @ApplicationContext context: Context,
    ) = GetShouldIReversePodcastOrderUseCase(context)

    @Provides
    @Singleton
    fun provideSetShouldIInvestPodcastOrderUseCase(
        @ApplicationContext context: Context,
    ) = SetShouldIReversePodcastOrderUseCase(context)
}
