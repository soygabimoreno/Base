package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.domain.repository.senioraudio.SeniorAudioRepository
import soy.gabimoreno.domain.usecase.GetSeniorAudioStreamUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SeniorAudioModule {
    @Provides
    @Singleton
    fun provideGetSeniorStreamUseCase(
        seniorAudioRepository: SeniorAudioRepository,
        @ApplicationContext context: Context,
    ) = GetSeniorAudioStreamUseCase(
        context = context,
        seniorAudioRepository = seniorAudioRepository,
    )
}
