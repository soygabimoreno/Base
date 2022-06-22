package soy.gabimoreno

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
import androidx.navigation.navDeepLink
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import soy.gabimoreno.ui.common.ProvideMultiViewModel
import soy.gabimoreno.ui.navigation.Destination
import soy.gabimoreno.ui.navigation.Navigator
import soy.gabimoreno.ui.navigation.ProvideNavHostController
import soy.gabimoreno.ui.screen.home.HomeScreen
import soy.gabimoreno.ui.screen.podcast.AudioBottomBar
import soy.gabimoreno.ui.screen.podcast.DetailScreen
import soy.gabimoreno.ui.screen.podcast.PlayerScreen
import soy.gabimoreno.ui.theme.GabiMorenoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GabiMoreno)
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
    startDestination: String = Destination.home,
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

                            composable(
                                Destination.podcast,
                                deepLinks = listOf(navDeepLink { uriPattern = "https://www.listennotes.com/e/{id}" })
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
