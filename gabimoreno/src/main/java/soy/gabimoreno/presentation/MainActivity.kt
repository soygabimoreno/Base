package soy.gabimoreno.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import soy.gabimoreno.R
import soy.gabimoreno.domain.usecase.GetDeepLinkContentUseCase
import soy.gabimoreno.framework.datastore.dataStore
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.navigation.Feature
import soy.gabimoreno.presentation.navigation.deeplink.DeepLinkEpisodeNumber
import soy.gabimoreno.presentation.navigation.deeplink.DeepLinkUrl

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            toast(R.string.app_will_show_notifications)
        } else {
            // Do nothing
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preloadDataFromDataStore()
        askNotificationPermission()
        setContent {
            // TODO: Manage deep links in a proper way
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

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Do nothing
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                toast(R.string.notifications_rationale)
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
