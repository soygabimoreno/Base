package soy.gabimoreno

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import soy.gabimoreno.presentation.navigation.Destination
import soy.gabimoreno.presentation.navigation.KEY_ID
import soy.gabimoreno.presentation.navigation.KEY_URL
import soy.gabimoreno.presentation.navigation.Navigator
import soy.gabimoreno.presentation.screen.detail.DetailScreen
import soy.gabimoreno.presentation.screen.home.HomeScreen
import soy.gabimoreno.presentation.screen.webview.WebViewScreen

@Composable
fun AppNavHost() {
    val navController = Navigator.current
    NavHost(
        navController = navController,
        startDestination = Destination.home
    ) {
        composable(route = Destination.home) {
            HomeScreen(
                onItemClicked = { episodeId ->
                    navController.navigate(Destination.detail(episodeId)) { }
                },
                onDeepLinkReceived = { episodeId ->
                    navController.navigate(Destination.detail(episodeId)) { }
                },
                onGoToWebClicked = { url ->
                    navController.navigate(Destination.webView(url)) { }
                }
            )
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

        composable(route = Destination.detail) { backStackEntry ->
            DetailScreen(
                podcastId = backStackEntry.arguments?.getString(KEY_ID)!!,
            )
        }

        composable(route = Destination.webView) { backStackEntry ->
            WebViewScreen(
                url = backStackEntry.arguments?.getString(KEY_URL)!!,
            )
        }
    }
}
