package soy.gabimoreno

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.insets.ProvideWindowInsets
import soy.gabimoreno.presentation.navigation.*
import soy.gabimoreno.presentation.screen.ProvideMultiViewModel
import soy.gabimoreno.presentation.screen.detail.DetailScreen
import soy.gabimoreno.presentation.screen.home.HomeScreen
import soy.gabimoreno.presentation.screen.player.PlayerScreen
import soy.gabimoreno.presentation.screen.webview.WebViewScreen
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.ui.AudioBottomBar

@Composable
fun GabiMorenoApp(
    backDispatcher: OnBackPressedDispatcher
) {
    GabiMorenoTheme {
        ProvideWindowInsets {
            ProvideMultiViewModel {
                ProvideNavHostController {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        NavHost(Navigator.current, Destination.home) {
                            composable(Destination.home) {
                                HomeScreen()
                            }

                            // TODO: Check how to manage deep links
//                            composable(
//                                Destination.detail,
//                                deepLinks = listOf(navDeepLink { uriPattern = "https://gabimoreno.soy/{id}" })
//                            ) { backStackEntry ->
//                                DetailScreen(
//                                    podcastId = backStackEntry.arguments?.getString("KEY_ID")!!,
//                                )
//                            }

                            composable(
                                Destination.detail
                            ) { backStackEntry ->
                                DetailScreen(
                                    podcastId = backStackEntry.arguments?.getString(KEY_ID)!!,
                                )
                            }

                            composable(
                                Destination.webView
                            ) { backStackEntry ->
                                WebViewScreen(
                                    url = backStackEntry.arguments?.getString(KEY_URL)!!,
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
