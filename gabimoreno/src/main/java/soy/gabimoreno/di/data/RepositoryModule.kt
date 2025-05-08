package soy.gabimoreno.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.data.local.audiocourse.LocalAudioCoursesDataSource
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.audiocourses.RemoteAudioCoursesDataSource
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCoursesRepository
import soy.gabimoreno.domain.repository.login.LoginRepository
import soy.gabimoreno.domain.repository.login.RemoteLoginRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.podcast.RemotePodcastRepository
import soy.gabimoreno.domain.repository.premiumaudios.DefaultPremiumAudiosRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
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
        saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase: SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
    ): PremiumAudiosRepository = DefaultPremiumAudiosRepository(
        localPremiumAudiosDataSource,
        remotePremiumAudiosDataSource,
        refreshPremiumAudiosFromRemoteUseCase,
        saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
    )

    @Provides
    @Singleton
    fun providePodcastRepository(
        podcastDatasource: PodcastDatasource,
        podcastUrl: PodcastUrl,
    ): PodcastRepository = RemotePodcastRepository(
        podcastDatasource,
        podcastUrl
    )

    @Provides
    @Singleton
    fun provideAudioCoursesRepository(
        localAudioCoursesDataSource: LocalAudioCoursesDataSource,
        remoteAudioCoursesDataSource: RemoteAudioCoursesDataSource,
        refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
    ): AudioCoursesRepository = DefaultAudioCoursesRepository(
        localAudioCoursesDataSource,
        remoteAudioCoursesDataSource,
        refreshPremiumAudiosFromRemoteUseCase
    )
}
