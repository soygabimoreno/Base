package soy.gabimoreno.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import soy.gabimoreno.data.cloud.audiosync.datasource.AudioCourseCloudDataSource
import soy.gabimoreno.data.cloud.audiosync.datasource.PodcastCloudDataSource
import soy.gabimoreno.data.cloud.audiosync.datasource.PremiumAudiosCloudDataSource
import soy.gabimoreno.data.cloud.playlist.datasource.CloudPlaylistDataSource
import soy.gabimoreno.data.local.audiocourse.LocalAudioCourseDataSource
import soy.gabimoreno.data.local.playlist.LocalPlaylistDataSource
import soy.gabimoreno.data.local.podcast.LocalPodcastDataSource
import soy.gabimoreno.data.local.podcast.LocalSeniorAudioDataSource
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudioDataSource
import soy.gabimoreno.data.remote.datasource.audiocourse.RemoteAudioCourseDataSource
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.data.remote.datasource.premiumaudio.RemotePremiumAudiosDataSource
import soy.gabimoreno.data.remote.datasource.senioraudio.RemoteSeniorAudioDatasource
import soy.gabimoreno.data.repository.podcast.DefaultPodcastRepository
import soy.gabimoreno.data.repository.senioraudio.DefaultSeniorAudioAudioRepository
import soy.gabimoreno.domain.repository.audiocourse.AudioCourseRepository
import soy.gabimoreno.domain.repository.audiocourse.DefaultAudioCourseRepository
import soy.gabimoreno.domain.repository.login.LoginRepository
import soy.gabimoreno.domain.repository.login.RemoteLoginRepository
import soy.gabimoreno.domain.repository.playlist.DefaultPlaylistRepository
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.premiumaudio.DefaultPremiumAudiosRepository
import soy.gabimoreno.domain.repository.premiumaudio.PremiumAudiosRepository
import soy.gabimoreno.domain.repository.senioraudio.SeniorAudioRepository
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosFromRemoteUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
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
        localPremiumAudioDataSource: LocalPremiumAudioDataSource,
        remotePremiumAudiosDataSource: RemotePremiumAudiosDataSource,
        refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
        saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase:
            SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
    ): PremiumAudiosRepository =
        DefaultPremiumAudiosRepository(
            cloudDataSource,
            localPremiumAudioDataSource,
            remotePremiumAudiosDataSource,
            refreshPremiumAudiosFromRemoteUseCase,
            saveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase,
        )

    @Provides
    @Singleton
    fun provideDefaultPodcastRepository(
        cloudDataSource: PodcastCloudDataSource,
        localPodcastDatasource: LocalPodcastDataSource,
        podcastUrl: String,
        remotePodcastRepository: PodcastDatasource,
        scope: CoroutineScope,
    ): PodcastRepository =
        DefaultPodcastRepository(
            cloudDataSource = cloudDataSource,
            localPodcastDatasource = localPodcastDatasource,
            podcastUrl = podcastUrl,
            remotePodcastRepository = remotePodcastRepository,
            scope = scope,
        )

    @Provides
    @Singleton
    fun provideDefaultSeniorRepository(
        localSeniorAudioDataSource: LocalSeniorAudioDataSource,
        remoteSeniorAudioDatasource: RemoteSeniorAudioDatasource,
        remoteConfigProvider: RemoteConfigProvider,
        scope: CoroutineScope,
    ): SeniorAudioRepository =
        DefaultSeniorAudioAudioRepository(
            localSeniorAudioDataSource = localSeniorAudioDataSource,
            remoteSeniorAudioDataSource = remoteSeniorAudioDatasource,
            remoteConfigProvider = remoteConfigProvider,
            scope = scope,
        )

    @Provides
    @Singleton
    fun provideAudioCoursesRepository(
        cloudDataSource: AudioCourseCloudDataSource,
        localAudioCourseDataSource: LocalAudioCourseDataSource,
        remoteAudioCoursesDataSource: RemoteAudioCourseDataSource,
        refreshPremiumAudiosFromRemoteUseCase: RefreshPremiumAudiosFromRemoteUseCase,
    ): AudioCourseRepository =
        DefaultAudioCourseRepository(
            cloudDataSource,
            localAudioCourseDataSource,
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
