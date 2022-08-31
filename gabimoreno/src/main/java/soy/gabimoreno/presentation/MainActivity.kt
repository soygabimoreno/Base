package soy.gabimoreno.presentation

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import soy.gabimoreno.domain.usecase.GetDeepLinkContentUseCase
import soy.gabimoreno.presentation.navigation.deeplink.DeepLinkEpisodeNumber
import soy.gabimoreno.presentation.navigation.deeplink.DeepLinkUrl


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Check how to manage deep links
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        if (action != null && data != null) {
            val parameters: List<String> = data.pathSegments
            val getDeepLinkContentUseCase = GetDeepLinkContentUseCase()
            val deepLinkContent = getDeepLinkContentUseCase(parameters)
            DeepLinkEpisodeNumber.value = deepLinkContent.episodeNumber
            DeepLinkUrl.value = deepLinkContent.url
        }

//        val episodeNumber = if (action != null && data != null) {
//            val parameters: List<String> = data.pathSegments
//            if (parameters.isNotEmpty()) {
//                parameters[0].toIntOrNull()
//            } else null
//        } else null
//        DeepLinkEpisodeNumber.value = episodeNumber


        setContent {
            GabiMorenoApp(
                backDispatcher = onBackPressedDispatcher
            )
        }
    }
}
