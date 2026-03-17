package soy.gabimoreno.data.remote.survey.model

data class SurveyApiModel(
    val description: String = EMPTY_STRING,
    val id: Long = 0L,
    val active: Boolean = false,
    val url: String = EMPTY_STRING,
)

private const val EMPTY_STRING = ""
