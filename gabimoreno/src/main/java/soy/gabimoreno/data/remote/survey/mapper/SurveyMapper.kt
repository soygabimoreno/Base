package soy.gabimoreno.data.remote.survey.mapper

import soy.gabimoreno.data.remote.survey.model.SurveyApiModel
import soy.gabimoreno.domain.model.survey.Survey

fun SurveyApiModel.toDomain(): Survey =
    Survey(
        description = description,
        id = id.toInt(),
        isActive = active,
        url = url,
    )
