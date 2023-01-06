package soy.gabimoreno.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.data.local.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.domain.repository.login.LoginRepository
import soy.gabimoreno.domain.repository.login.RemoteLoginRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.podcast.RemotePodcastRepository
import soy.gabimoreno.domain.repository.premiumaudios.DefaultPremiumAudiosRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginDatasource: LoginDatasource,
    ): LoginRepository = RemoteLoginRepository(loginDatasource)

    @Provides
    @Singleton
    fun provideContentRepository(
        localPremiumAudiosDataSource: LocalPremiumAudiosDataSource,
        remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource,
        refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
    ): PremiumAudiosRepository = DefaultPremiumAudiosRepository(
        localPremiumAudiosDataSource,
        remotePremiumAudiosDataSource,
        refreshPremiumAudiosFromRemoteUseCase
    )

    @Provides
    @Singleton
    fun providePodcastRepository(
        podcastDatasource: PodcastDatasource,
    ): PodcastRepository = RemotePodcastRepository(podcastDatasource)
}
