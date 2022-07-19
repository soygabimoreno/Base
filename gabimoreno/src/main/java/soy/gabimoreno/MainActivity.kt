package soy.gabimoreno

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import soy.gabimoreno.presentation.navigation.Destination
import soy.gabimoreno.presentation.navigation.EpisodeNumber
import soy.gabimoreno.presentation.navigation.Navigator
import soy.gabimoreno.presentation.navigation.ProvideNavHostController
import soy.gabimoreno.presentation.screen.ProvideMultiViewModel
import soy.gabimoreno.presentation.screen.detail.DetailScreen
import soy.gabimoreno.presentation.screen.home.HomeScreen
import soy.gabimoreno.presentation.screen.player.PlayerScreen
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.ui.AudioBottomBar


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // TODO: Check how to manage deep links
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        val episodeNumber = if (action != null && data != null) {
            val parameters: List<String> = data.pathSegments
            if (parameters.isNotEmpty()) {
                parameters[0].toIntOrNull()
            } else null
        } else null
        EpisodeNumber.value = episodeNumber


        setContent {
            GabiMorenoApp(
                startDestination = Destination.home,
                backDispatcher = onBackPressedDispatcher
            )
        }
    }
}

@Composable
fun GabiMorenoApp(
    startDestination: String,
    backDispatcher: OnBackPressedDispatcher
) {
    GabiMorenoTheme {
        ProvideWindowInsets {
            ProvideMultiViewModel {
                ProvideNavHostController {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        NavHost(Navigator.current, startDestination) {
                            composable(Destination.home) {
                                HomeScreen()
                            }

                            // TODO: Check how to manage deep links
//                            composable(
//                                Destination.detail,
//                                deepLinks = listOf(navDeepLink { uriPattern = "https://gabimoreno.soy/{id}" })
//                            ) { backStackEntry ->
//                                DetailScreen(
//                                    podcastId = backStackEntry.arguments?.getString("id")!!,
//                                )
//                            }

                            composable(
                                Destination.detail
                            ) { backStackEntry ->
                                DetailScreen(
                                    podcastId = backStackEntry.arguments?.getString("id")!!,
                                )
                            }
                        }
                        AudioBottomBar(
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                        PlayerScreen(backDispatcher)
                    }
                }
            }
        }
    }
}
