package soy.gabimoreno.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import soy.gabimoreno.data.cloud.audiosync.datasource.AudioCoursesCloudDataSource
import soy.gabimoreno.data.cloud.audiosync.datasource.PremiumAudiosCloudDataSource
import soy.gabimoreno.data.cloud.playlist.datasource.CloudPlaylistDataSource
import soy.gabimoreno.data.local.audiocourse.LocalAudioCoursesDataSource
import soy.gabimoreno.data.local.playlist.LocalPlaylistDataSource
import soy.gabimoreno.data.local.podcast.LocalPodcastDataSource
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.audiocourses.RemoteAudioCoursesDataSource
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.data.remote.datasource.premiumaudios.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.repository.podcast.DefaultPodcastRepository
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.audiocourses.DefaultAudioCoursesRepository
import soy.gabimoreno.domain.repository.login.LoginRepository
import soy.gabimoreno.domain.repository.login.RemoteLoginRepository
import soy.gabimoreno.domain.repository.playlist.DefaultPlaylistRepository
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
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
    fun provideLoginRepository(loginDatasource: LoginDatasource): LoginRepository =
        RemoteLoginRepository(loginDatasource)

    @Provides
    @Singleton
    fun provideContentRepository(
        cloudDataSource: PremiumAudiosCloudDataSource,
        localPremiumAudiosDataSource: LocalPremiumAudiosDataSource,
        remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource,
        refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
        saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase:
            SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
    ): PremiumAudiosRepository =
        DefaultPremiumAudiosRepository(
            cloudDataSource,
            localPremiumAudiosDataSource,
            remotePremiumAudiosDataSource,
            refreshPremiumAudiosFromRemoteUseCase,
            saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
        )

    @Provides
    @Singleton
    fun provideDefaultPodcastRepository(
        localPodcastDatasource: LocalPodcastDataSource,
        podcastUrl: String,
        remotePodcastRepository: PodcastDatasource,
        scope: CoroutineScope,
    ): PodcastRepository =
        DefaultPodcastRepository(
            localPodcastDatasource,
            podcastUrl,
            remotePodcastRepository,
            scope,
        )

    @Provides
    @Singleton
    fun provideAudioCoursesRepository(
        cloudDataSource: AudioCoursesCloudDataSource,
        localAudioCoursesDataSource: LocalAudioCoursesDataSource,
        remoteAudioCoursesDataSource: RemoteAudioCoursesDataSource,
        refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
    ): AudioCoursesRepository =
        DefaultAudioCoursesRepository(
            cloudDataSource,
            localAudioCoursesDataSource,
            remoteAudioCoursesDataSource,
            refreshPremiumAudiosFromRemoteUseCase,
        )

    @Provides
    @Singleton
    fun providePlaylistRepository(
        cloudDataSource: CloudPlaylistDataSource,
        localPlaylistDataSource: LocalPlaylistDataSource,
    ): PlaylistRepository =
        DefaultPlaylistRepository(
            cloudDataSource,
            localPlaylistDataSource,
        )
}
