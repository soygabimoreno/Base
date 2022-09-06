package soy.gabimoreno.presentation

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import soy.gabimoreno.domain.usecase.GetDeepLinkContentUseCase
import soy.gabimoreno.framework.datastore.dataStore
import soy.gabimoreno.presentation.navigation.Feature
import soy.gabimoreno.presentation.navigation.deeplink.DeepLinkEpisodeNumber
import soy.gabimoreno.presentation.navigation.deeplink.DeepLinkUrl

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preloadDataFromDataStore()

        setContent {
            // TODO: Check how to manage deep links
            val action: String? = intent?.action
            val data: Uri? = intent?.data
            if (action != null && data != null) {
                val parameters: List<String> = data.pathSegments
                val getDeepLinkContentUseCase = GetDeepLinkContentUseCase()
                val deepLinkContent = getDeepLinkContentUseCase(parameters)
                DeepLinkEpisodeNumber.value = deepLinkContent.episodeNumber
                DeepLinkUrl.value = deepLinkContent.url

                val appState = rememberAppState()
                appState.setStartDestination(Feature.PODCAST)
            }

            AppUi(
                backDispatcher = onBackPressedDispatcher
            )
        }
    }

    private fun preloadDataFromDataStore() {
        lifecycleScope.launch {
            dataStore.data.first()
        }
    }
}
