package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.domain.usecase.DeleteAllPlaylistUseCase
import soy.gabimoreno.domain.usecase.DeletePlaylistByIdUseCase
import soy.gabimoreno.domain.usecase.DeletePlaylistItemByIdUseCase
import soy.gabimoreno.domain.usecase.GetAllPlaylistUseCase
import soy.gabimoreno.domain.usecase.InsertPlaylistUseCase
import soy.gabimoreno.domain.usecase.ResetPlaylistByIdUseCase
import soy.gabimoreno.domain.usecase.SavePlaylistUseCase
import soy.gabimoreno.domain.usecase.SetPlaylistItemsUseCase
import soy.gabimoreno.domain.usecase.UpdatePlaylistItemsUseCase
import soy.gabimoreno.domain.usecase.UpsertPlaylistsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaylistModule {
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
