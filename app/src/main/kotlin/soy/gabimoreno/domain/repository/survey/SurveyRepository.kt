package soy.gabimoreno.domain.repository.survey

import arrow.core.Either
import soy.gabimoreno.domain.model.survey.Survey

interface SurveyRepository {
    suspend fun getLastedSurvey(): Either<Throwable, Survey?>
}
