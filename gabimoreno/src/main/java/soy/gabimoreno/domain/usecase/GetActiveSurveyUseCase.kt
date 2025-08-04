package soy.gabimoreno.domain.usecase

import arrow.core.Either
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.model.survey.Survey
import soy.gabimoreno.domain.repository.survey.SurveyRepository
import javax.inject.Inject

class GetActiveSurveyUseCase
    @Inject
    constructor(
        private val getLastSurveyIdUseCase: GetLastSurveyIdUseCase,
        private val surveyRepository: SurveyRepository,
    ) {
        suspend operator fun invoke(): Either<Throwable, Survey?> {
            val lastSurveyId = getLastSurveyIdUseCase().first()
            surveyRepository
                .getLastedSurvey()
                .onRight { survey ->
                    if (survey != null && survey.id != lastSurveyId && survey.isActive) {
                        return Either.Right(survey)
                    }
                }
            return Either.Left(Throwable())
        }
    }
