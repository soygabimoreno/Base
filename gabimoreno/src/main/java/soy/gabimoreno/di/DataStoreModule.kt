package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.domain.session.MemberSession
import soy.gabimoreno.domain.usecase.CheckShouldIShowInAppReviewUseCase
import soy.gabimoreno.domain.usecase.DeleteAllPlaylistUseCase
import soy.gabimoreno.domain.usecase.DeletePlaylistByIdUseCase
import soy.gabimoreno.domain.usecase.DeletePlaylistItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetAllPlaylistUseCase
import soy.gabimoreno.domain.usecase.GetAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.GetInAppReviewCounterUseCase
import soy.gabimoreno.domain.usecase.GetLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudiosManagedUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReversePodcastOrderUseCase
import soy.gabimoreno.domain.usecase.InsertPlaylistUseCase
import soy.gabimoreno.domain.usecase.IsBearerTokenValid
import soy.gabimoreno.domain.usecase.MarkAudioCourseItemAsListenedUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase
import soy.gabimoreno.domain.usecase.ResetJwtAuthTokenUseCase
import soy.gabimoreno.domain.usecase.ResetPlaylistByIdUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase
import soy.gabimoreno.domain.usecase.SaveLastPremiumAudiosFromRemoteRequestTimeMillisInDataStoreUseCase
import soy.gabimoreno.domain.usecase.SavePlaylistUseCase
import soy.gabimoreno.domain.usecase.SetAllAudiocoursesAsUnlistenedUseCase
import soy.gabimoreno.domain.usecase.SetAllPremiumAudiosAsUnlistenedUseCase
import soy.gabimoreno.domain.usecase.SetJwtAuthTokenUseCase
import soy.gabimoreno.domain.usecase.SetPlaylistItemsUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReversePodcastOrderUseCase
import soy.gabimoreno.domain.usecase.SetShouldIShowInAppReviewUseCase
import soy.gabimoreno.domain.usecase.UpdateAudioItemFavoriteStateUseCase
import soy.gabimoreno.domain.usecase.UpdatePlaylistItemsUseCase
import soy.gabimoreno.domain.usecase.UpsertPlaylistsUseCase
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

    @Provides
    @Singleton
    fun provideGetShouldIShowInAppReviewUseCase(
        @ApplicationContext context: Context,
    ) = CheckShouldIShowInAppReviewUseCase(context)

    @Provides
    @Singleton
    fun provideGetInAppReviewCounterUseCase(
        @ApplicationContext context: Context,
    ) = GetInAppReviewCounterUseCase(context)

    @Provides
    @Singleton
    fun provideSetShouldIShowInAppReviewUseCase(
        @ApplicationContext context: Context,
    ) = SetShouldIShowInAppReviewUseCase(context)

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
    fun provideGetAudioCoursesUseCase(
        audioCoursesRepository: AudioCoursesRepository,
        @ApplicationContext context: Context,
    ): GetAudioCoursesUseCase =
        GetAudioCoursesUseCase(context = context, audioCoursesRepository = audioCoursesRepository)

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

    @Provides
    @Singleton
    fun provideDeleteAllPlaylistUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): DeleteAllPlaylistUseCase =
        DeleteAllPlaylistUseCase(
            context = context,
            playlistRepository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideDeletePlaylistByIdUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): DeletePlaylistByIdUseCase =
        DeletePlaylistByIdUseCase(
            context = context,
            playlistRepository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideDeletePlaylistItemByIdUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): DeletePlaylistItemByIdUseCase =
        DeletePlaylistItemByIdUseCase(
            context = context,
            playlistRepository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideGetAllPlaylistUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): GetAllPlaylistUseCase =
        GetAllPlaylistUseCase(
            context = context,
            playlistRepository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideResetPlaylistByIdUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): ResetPlaylistByIdUseCase =
        ResetPlaylistByIdUseCase(
            context = context,
            playlistRepository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideSavePlaylistUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): SavePlaylistUseCase =
        SavePlaylistUseCase(
            context = context,
            playlistRepository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideSetPlaylistItemsUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): SetPlaylistItemsUseCase =
        SetPlaylistItemsUseCase(
            context = context,
            repository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideUpdatePlaylistItemsUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): UpdatePlaylistItemsUseCase =
        UpdatePlaylistItemsUseCase(
            context = context,
            repository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideUpsertPlaylistsUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): UpsertPlaylistsUseCase =
        UpsertPlaylistsUseCase(
            context = context,
            repository = playlistRepository,
        )

    @Provides
    @Singleton
    fun provideInsertPlaylistUseCase(
        @ApplicationContext context: Context,
        playlistRepository: PlaylistRepository,
    ): InsertPlaylistUseCase =
        InsertPlaylistUseCase(
            context = context,
            playlistRepository = playlistRepository,
        )
}
