package soy.gabimoreno

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import soy.gabimoreno.presentation.navigation.Feature
import soy.gabimoreno.presentation.navigation.NavArg
import soy.gabimoreno.presentation.navigation.NavItem
import soy.gabimoreno.presentation.screen.detail.DetailScreen
import soy.gabimoreno.presentation.screen.home.HomeScreen
import soy.gabimoreno.presentation.screen.webview.WebViewScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Feature.PODCAST.route
    ) {
        podcastNav(navController)
    }
}

private fun NavGraphBuilder.podcastNav(
    navController: NavController
) {
    navigation(
        startDestination = NavItem.ContentType(Feature.PODCAST).route,
        route = Feature.PODCAST.route
    ) {
        composable(navItem = NavItem.ContentType(Feature.PODCAST)) {
            HomeScreen(
                onItemClicked = { episodeId ->
                    navController.navigateToDetail(episodeId)
                },
                onDeepLinkReceived = { episodeId ->
                    navController.navigateToDetail(episodeId)
                },
                onGoToWebClicked = { encodedUrl ->
                    navController.navigateToWebView(encodedUrl)
                }
            )
        }

        // TODO: Check how to manage deep links
        // deepLinks = listOf(navDeepLink { uriPattern = "https://gabimoreno.soy/{id}" })
        composable(navItem = NavItem.ContentDetail(Feature.PODCAST)) {
            DetailScreen(
                podcastId = it.findArg(NavArg.EpisodeId),
                onBackClicked = {
                    navController.goBack()
                }
            )
        }

        composable(navItem = NavItem.ContentWebView(Feature.PODCAST)) {
            WebViewScreen(
                url = it.findArg(NavArg.EncodedUrl),
                onBackClicked = {
                    navController.goBack()
                }
            )
        }
    }
}

private fun NavController.navigateToDetail(episodeId: String) {
    navigate(route = NavItem.ContentDetail(Feature.PODCAST).createRoute(episodeId))
}

private fun NavController.navigateToWebView(encodedUrl: String) {
    navigate(route = NavItem.ContentWebView(Feature.PODCAST).createRoute(encodedUrl))
}

private fun NavController.goBack() {
    popBackStack()
}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args
    ) {
        content(it)
    }
}

private inline fun <reified T> NavBackStackEntry.findArg(arg: NavArg): T {
    val value = arguments?.get(arg.key)
    requireNotNull(value)
    return value as T
}
