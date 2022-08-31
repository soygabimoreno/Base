package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL
import javax.inject.Inject

class GetDeepLinkContentUseCase @Inject constructor() {
    operator fun invoke(parameters: List<String>): DeepLinkContent {
        val episodeNumber = if (parameters.isNotEmpty()) {
            parameters[0].toIntOrNull()
        } else null

        val url = if (episodeNumber != null) {
            null
        } else {
            val baseUrl = GABI_MORENO_WEB_BASE_URL
            if (parameters.isNotEmpty()) {
                val stringBuilder = StringBuilder().append(baseUrl)
                parameters.forEach {
                    stringBuilder.append("/$it")
                }
                stringBuilder.toString()
            } else {
                baseUrl
            }
        }

        return DeepLinkContent(
            episodeNumber = episodeNumber,
            url = url
        )
    }
}

data class DeepLinkContent(
    val episodeNumber: Int?,
    val url: String?
)
