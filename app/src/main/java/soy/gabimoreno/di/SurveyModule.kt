package soy.gabimoreno.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.data.remote.survey.DefaultSurveyRepository
import soy.gabimoreno.domain.repository.survey.SurveyRepository
import soy.gabimoreno.domain.usecase.GetActiveSurveyUseCase
import soy.gabimoreno.domain.usecase.GetLastSurveyIdUseCase
import soy.gabimoreno.domain.usecase.SetLastSurveyIdUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SurveyModule {
    @Provides
    @Singleton
    fun provideSurveyRepository(firestore: FirebaseFirestore): SurveyRepository =
        DefaultSurveyRepository(firestore)

    @Provides
    @Singleton
    fun provideGetLastSurveyIdUseCase(
        @ApplicationContext context: Context,
    ): GetLastSurveyIdUseCase = GetLastSurveyIdUseCase(context)

    @Provides
    @Singleton
    fun provideSetLastSurveyIdUseCase(
        @ApplicationContext context: Context,
    ): SetLastSurveyIdUseCase = SetLastSurveyIdUseCase(context)

    @Provides
    @Singleton
    fun provideGetActiveSurveyUseCase(
        getLastSurveyIdUseCase: GetLastSurveyIdUseCase,
        surveyRepository: SurveyRepository,
    ): GetActiveSurveyUseCase =
        GetActiveSurveyUseCase(
            getLastSurveyIdUseCase = getLastSurveyIdUseCase,
            surveyRepository = surveyRepository,
        )
}
