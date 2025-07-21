package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.data.remote.datasource.login.LoginDatasource
import soy.gabimoreno.domain.usecase.RefreshBearerTokenUseCase
import soy.gabimoreno.domain.usecase.ResetJwtAuthTokenUseCase
import soy.gabimoreno.domain.usecase.SetJwtAuthTokenUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RefreshTokenModule {
    @Provides
    @Singleton
    fun provideRefreshBearerTokenUseCase(
        loginDatasource: LoginDatasource,
        setJwtAuthTokenUseCase: SetJwtAuthTokenUseCase,
        resetJwtAuthTokenUseCase: ResetJwtAuthTokenUseCase,
        @ApplicationContext context: Context,
    ) = RefreshBearerTokenUseCase(
        loginDatasource,
        setJwtAuthTokenUseCase,
        resetJwtAuthTokenUseCase,
        context,
    )
}
