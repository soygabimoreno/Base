package soy.gabimoreno.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import soy.gabimoreno.presentation.AppState
import soy.gabimoreno.presentation.screen.courses.detail.AudioCoursesDetailScreenRoot
import soy.gabimoreno.presentation.screen.courses.list.AudioCoursesListScreenRoot
import soy.gabimoreno.presentation.screen.detail.DetailScreen
import soy.gabimoreno.presentation.screen.home.HomeScreen
import soy.gabimoreno.presentation.screen.premium.PremiumScreenRoot
import soy.gabimoreno.presentation.screen.webview.WebViewScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    appState: AppState,
) {
    NavHost(
        navController = navController,
        startDestination = appState.startDestination
    ) {
        podcastNav(navController, appState)
        premiumNav(navController, appState)
        audioCoursesNav(navController, appState)
    }
}

private fun NavGraphBuilder.podcastNav(
    navController: NavController,
    appState: AppState,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.PODCAST).route,
        route = Feature.PODCAST.route
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PODCAST)) {
            appState.setStartDestination(Feature.PODCAST)
            HomeScreen(
                onItemClicked = { episodeId ->
                    navController.navigateToDetailFromPodcast(episodeId)
                },
                onDeepLinkReceived = { episodeId ->
                    navController.navigateToDetailFromPodcast(episodeId)
                },
                onGoToWebClicked = { encodedUrl ->
                    navController.navigateToWebView(encodedUrl)
                }
            )
        }

        // TODO: Manage deep links in a proper way
        // deepLinks = listOf(navDeepLink { uriPattern = "https://gabimoreno.soy/{id}" })
        composable(
            navCommand = NavCommand.ContentDetail(
                Feature.PODCAST,
                listOf(NavArg.EpisodeId)
            )
        ) {
            DetailScreen(
                audioId = it.findArg(NavArg.EpisodeId),
                Feature.PODCAST,
                onBackClicked = {
                    navController.goBack()
                }
            )
        }

        composable(navCommand = NavCommand.ContentWebView(Feature.PODCAST)) {
            WebViewScreen(
                url = it.findArg(NavArg.EncodedUrl),
                onBackClicked = {
                    navController.goBack()
                }
            )
        }
    }
}

private fun NavGraphBuilder.premiumNav(
    navController: NavController,
    appState: AppState,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.PREMIUM).route,
        route = Feature.PREMIUM.route
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PREMIUM)) {
            appState.setStartDestination(Feature.PREMIUM)
            PremiumScreenRoot { premiumAudioId ->
                navController.navigateToDetailFromPremium(premiumAudioId)
            }
        }
        composable(
            navCommand = NavCommand.ContentDetail(
                Feature.PREMIUM,
                listOf(NavArg.PremiumAudioId)
            )
        ) {
            DetailScreen(
                audioId = it.findArg(NavArg.PremiumAudioId),
                Feature.PREMIUM,
                onBackClicked = {
                    navController.goBack()
                }
            )
        }
    }
}

private fun NavGraphBuilder.audioCoursesNav(
    navController: NavController,
    appState: AppState,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.AUDIOCOURSES).route,
        route = Feature.AUDIOCOURSES.route
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.AUDIOCOURSES)) {
            appState.setStartDestination(Feature.AUDIOCOURSES)
            AudioCoursesListScreenRoot { audioCourseId ->
                navController.navigateToAudioCourseDetailFromPremium(audioCourseId)
            }
        }
        composable(
            navCommand = NavCommand.ContentCoursesDetail(
                Feature.AUDIOCOURSES,
                listOf(NavArg.AudioCourseId)
            )
        ) {
            AudioCoursesDetailScreenRoot(
                audioCourseId = it.findArg(NavArg.AudioCourseId),
                onBackClicked = {
                    navController.goBack()
                }
            )
        }
    }
}

private fun NavController.navigateToDetailFromPodcast(episodeId: String) {
    navigate(
        route = NavCommand.ContentDetail(Feature.PODCAST, listOf(NavArg.EpisodeId))
            .createRoute(episodeId)
    )
}

private fun NavController.navigateToDetailFromPremium(premiumAudioId: String) {
    navigate(
        route = NavCommand.ContentDetail(Feature.PREMIUM, listOf(NavArg.PremiumAudioId))
            .createRoute(premiumAudioId)
    )
}

private fun NavController.navigateToAudioCourseDetailFromPremium(audioCourseId: String) {
    navigate(
        route = NavCommand.ContentCoursesDetail(Feature.AUDIOCOURSES, listOf(NavArg.AudioCourseId))
            .createRoute(audioCourseId)
    )
}


private fun NavController.navigateToWebView(encodedUrl: String) {
    navigate(route = NavCommand.ContentWebView(Feature.PODCAST).createRoute(encodedUrl))
}

private fun NavController.goBack() {
    popBackStack()
}

private fun NavGraphBuilder.composable(
    navCommand: NavCommand,
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = navCommand.route,
        arguments = navCommand.args
    ) {
        content(it)
    }
}

private inline fun <reified T> NavBackStackEntry.findArg(arg: NavArg): T {
    val arguments = requireNotNull(arguments) { "No arguments found in NavBackStackEntry" }
    return when (T::class) {
        String::class -> arguments.getString(arg.key) as T
        Int::class -> arguments.getInt(arg.key) as T
        Long::class -> arguments.getLong(arg.key) as T
        Boolean::class -> arguments.getBoolean(arg.key) as T
        Float::class -> arguments.getFloat(arg.key) as T
        Double::class -> arguments.getDouble(arg.key) as T
        else -> throw IllegalArgumentException("Unsupported argument type")
    }
}
