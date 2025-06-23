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
import soy.gabimoreno.presentation.screen.playlist.audio.PlaylistAudioItemRoot
import soy.gabimoreno.presentation.screen.playlist.detail.PlaylistDetailScreenRoot
import soy.gabimoreno.presentation.screen.playlist.list.PlaylistScreenRoot
import soy.gabimoreno.presentation.screen.premium.PremiumScreenRoot
import soy.gabimoreno.presentation.screen.profile.ProfileScreenRoot
import soy.gabimoreno.presentation.screen.webview.WebViewScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    appState: AppState,
    onRequireAuth: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = appState.startDestination
    ) {
        podcastNav(navController, appState)
        premiumNav(navController, appState, onRequireAuth)
        audioCoursesNav(navController, appState)
        profileNav(navController, appState, onRequireAuth)
        playlistNav(navController, appState)
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

        val command = NavCommand.ContentDetail(Feature.PODCAST, listOf(NavArg.EpisodeId))
        composable(
            route = command.route,
            arguments = command.arguments,
            deepLinks = command.deepLinks
        ) {
            DetailScreen(
                audioId = it.findArg<String>(NavArg.EpisodeId),
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
    onRequireAuth: () -> Unit,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.PREMIUM).route,
        route = Feature.PREMIUM.route
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PREMIUM)) {
            appState.setStartDestination(Feature.PREMIUM)
            PremiumScreenRoot(
                onRequireAuth = onRequireAuth,
                onItemClicked = { premiumAudioId ->
                    navController.navigateToDetailFromPremium(premiumAudioId)
                },
                onPlaylistClicked = {
                    navController.navigate(
                        route = NavCommand.ContentType(Feature.PLAYLISTS).route
                    )
                },
                onAddToPlaylistClicked = { audioItemId ->
                    navController.navigateToPlaylistAudioItem(audioItemId = audioItemId)
                }
            )
        }

        val command = NavCommand.ContentDetail(Feature.PREMIUM, listOf(NavArg.PremiumAudioId))
        composable(
            route = command.route,
            arguments = command.arguments,
            deepLinks = command.deepLinks
        ) {
            DetailScreen(
                audioId = it.findArg<String>(NavArg.PremiumAudioId),
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
            AudioCoursesListScreenRoot(
                onItemClicked = { audioCourseId ->
                    navController.navigateToAudioCourseDetailFromAudiocourses(audioCourseId)
                },
                onPlaylistClicked = {
                    navController.navigate(
                        route = NavCommand.ContentType(Feature.PLAYLISTS).route
                    )
                },
            )
        }
        val command =
            NavCommand.ContentCoursesDetail(Feature.AUDIOCOURSES, listOf(NavArg.AudioCourseId))
        composable(
            route = command.route,
            arguments = command.arguments,
            deepLinks = command.deepLinks
        ) {
            AudioCoursesDetailScreenRoot(
                audioCourseId = it.findArg<String>(NavArg.AudioCourseId),
                onBackClicked = {
                    navController.goBack()
                },
                onAddToPlaylistClicked = { audioItemId ->
                    navController.navigateToPlaylistAudioItem(audioItemId = audioItemId)
                }
            )
        }
    }
}

private fun NavGraphBuilder.profileNav(
    navController: NavController,
    appState: AppState,
    onRequireAuth: () -> Unit,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.PROFILE).route,
        route = Feature.PROFILE.route
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PROFILE)) {
            appState.setStartDestination(Feature.PROFILE)
            ProfileScreenRoot(
                onPlaylistClicked = {
                    navController.navigate(
                        route = NavCommand.ContentType(Feature.PLAYLISTS).route
                    )
                },
                onToggleBottomSheet = onRequireAuth
            )
        }
    }
}

private fun NavGraphBuilder.playlistNav(
    navController: NavController,
    appState: AppState,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.PLAYLISTS).route,
        route = Feature.PLAYLISTS.route
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PLAYLISTS)) {
            appState.setStartDestination(Feature.PLAYLISTS)
            PlaylistScreenRoot(
                onBackClicked = {
                    navController.goBackOrNavigateTo(Feature.PROFILE.route)
                },
                onItemClick = { playlistId ->
                    navController.navigateToPlaylistDetailFromPlaylist(playlistId)
                }
            )
        }
        composable(
            navCommand = NavCommand.ContentPlaylistDetail(
                Feature.PLAYLISTS,
                listOf(NavArg.PlaylistId)
            )
        ) {
            PlaylistDetailScreenRoot(
                playlistId = it.findArg(NavArg.PlaylistId),
                onBackClicked = {
                    navController.goBack()
                }
            )
        }
        composable(
            navCommand = NavCommand.ContentAudioItemDetail(
                Feature.PLAYLISTS,
                listOf(NavArg.AudioItemId)
            )
        ) {
            PlaylistAudioItemRoot(
                playlistAudioId = it.findArg(NavArg.AudioItemId),
                onBackClicked = {
                    navController.goBack()
                },
                onNewPlaylistClicked = {
                    navController.navigate(
                        route = NavCommand.ContentType(Feature.PLAYLISTS).route
                    )
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

private fun NavController.navigateToAudioCourseDetailFromAudiocourses(audioCourseId: String) {
    navigate(
        route = NavCommand.ContentCoursesDetail(Feature.AUDIOCOURSES, listOf(NavArg.AudioCourseId))
            .createRoute(audioCourseId)
    )
}

private fun NavController.navigateToPlaylistDetailFromPlaylist(playlistId: String) {
    navigate(
        route = NavCommand.ContentPlaylistDetail(Feature.PLAYLISTS, listOf(NavArg.PlaylistId))
            .createRoute(playlistId)
    )
}

private fun NavController.navigateToPlaylistAudioItem(audioItemId: String) {
    navigate(
        route = NavCommand.ContentAudioItemDetail(Feature.PLAYLISTS, listOf(NavArg.AudioItemId))
            .createRoute(audioItemId)
    )
}

private fun NavController.navigateToWebView(encodedUrl: String) {
    navigate(route = NavCommand.ContentWebView(Feature.PODCAST).createRoute(encodedUrl))
}

private fun NavController.goBack() {
    popBackStack()
}

fun NavController.goBackOrNavigateTo(route: String) {
    val wasPopped = popBackStack()
    if (!wasPopped) navigate(route) {
        popUpTo(graph.startDestinationId) {
            inclusive = false
        }
        launchSingleTop = true
    }
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
