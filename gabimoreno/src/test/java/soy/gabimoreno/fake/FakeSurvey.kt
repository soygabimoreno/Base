package soy.gabimoreno.fake

import soy.gabimoreno.data.remote.survey.model.SurveyApiModel
import soy.gabimoreno.domain.model.survey.Survey

fun buildSurvey(
    isActive: Boolean = true,
    surveyId: Int = 1,
) = Survey(
    description = "Which language do you prefer, Java or Kotlin?",
    isActive = isActive,
    id = surveyId,
    url = "https://gabimoreno.soy",
)

fun buildSurveyApiModel(
    active: Boolean = true,
    surveyId: Long = 1L,
) = SurveyApiModel(
    description = "Which language do you prefer, Java or Kotlin?",
    active = active,
    id = surveyId,
    url = "https://gabimoreno.soy",
)
