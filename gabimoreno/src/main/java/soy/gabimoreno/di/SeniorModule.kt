package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.senior.SeniorRepository
import soy.gabimoreno.domain.usecase.GetSeniorStreamUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SeniorModule {
    @Provides
    @Singleton
    fun provideGetSeniorStreamUseCase(
        seniorRepository: SeniorRepository,
        @ApplicationContext context: Context,
    ) = GetSeniorStreamUseCase(
        context = context,
        seniorRepository = seniorRepository,
    )
}
