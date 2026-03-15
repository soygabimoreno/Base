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
import soy.gabimoreno.presentation.screen.audiocourses.detail.AudioCoursesDetailScreenRoot
import soy.gabimoreno.presentation.screen.audiocourses.list.AudioCoursesScreenRoot
import soy.gabimoreno.presentation.screen.detail.DetailScreen
import soy.gabimoreno.presentation.screen.home.HomeScreen
import soy.gabimoreno.presentation.screen.playlist.audio.PlaylistAudioItemRoot
import soy.gabimoreno.presentation.screen.playlist.detail.PlaylistDetailScreenRoot
import soy.gabimoreno.presentation.screen.playlist.list.PlaylistScreenRoot
import soy.gabimoreno.presentation.screen.premiumaudios.PremiumAudiosScreenRoot
import soy.gabimoreno.presentation.screen.profile.ProfileScreenRoot
import soy.gabimoreno.presentation.screen.senioraudios.SeniorAudiosScreen
import soy.gabimoreno.presentation.screen.webview.WebViewScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    appState: AppState,
    onRequireAuth: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = appState.startDestination,
    ) {
        seniorNav(navController, appState)
        podcastNav(navController, appState)
        premiumNav(navController, appState, onRequireAuth)
        audioCoursesNav(navController, appState)
        profileNav(navController, appState, onRequireAuth)
        playlistNav(navController, appState)
    }
}

private fun NavGraphBuilder.seniorNav(
    navController: NavController,
    appState: AppState,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.SENIOR_AUDIOS).route,
        route = Feature.SENIOR_AUDIOS.route,
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.SENIOR_AUDIOS)) {
            appState.setStartDestination(Feature.SENIOR_AUDIOS)
            SeniorAudiosScreen(
                onItemClicked = { episodeId ->
                    navController.navigateToDetailFromSenior(episodeId)
                },
                onGoToWebClicked = { encodedUrl ->
                    navController.navigateToWebView(encodedUrl)
                },
            )
        }

        val command = NavCommand.ContentDetail(Feature.SENIOR_AUDIOS, listOf(NavArg.EpisodeId))
        composable(
            route = command.route,
            arguments = command.arguments,
            deepLinks = command.deepLinks,
        ) {
            DetailScreen(
                audioId = it.findArg<String>(NavArg.EpisodeId),
                Feature.SENIOR_AUDIOS,
                onBackClicked = {
                    navController.goBack()
                },
                onAddToPlaylistClicked = { audioItemId ->
                    navController.navigateToPlaylistAudioItem(audioItemId = audioItemId)
                },
            )
        }

        composable(navCommand = NavCommand.ContentWebView(Feature.SENIOR_AUDIOS)) {
            WebViewScreen(
                url = it.findArg(NavArg.EncodedUrl),
                onBackClicked = {
                    navController.goBack()
                },
            )
        }
    }
}

private fun NavGraphBuilder.podcastNav(
    navController: NavController,
    appState: AppState,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.PODCAST).route,
        route = Feature.PODCAST.route,
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PODCAST)) {
            appState.setStartDestination(Feature.PODCAST)
            HomeScreen(
                onItemClicked = { episodeId ->
                    navController.navigateToDetailFromPodcast(episodeId)
                },
                onGoToWebClicked = { encodedUrl ->
                    navController.navigateToWebView(encodedUrl)
                },
                onAddToPlaylistClicked = { audioItemId ->
                    navController.navigateToPlaylistAudioItem(audioItemId = audioItemId)
                },
            )
        }

        val command = NavCommand.ContentDetail(Feature.PODCAST, listOf(NavArg.EpisodeId))
        composable(
            route = command.route,
            arguments = command.arguments,
            deepLinks = command.deepLinks,
        ) {
            DetailScreen(
                audioId = it.findArg<String>(NavArg.EpisodeId),
                Feature.PODCAST,
                onBackClicked = {
                    navController.goBack()
                },
                onAddToPlaylistClicked = { audioItemId ->
                    navController.navigateToPlaylistAudioItem(audioItemId = audioItemId)
                },
            )
        }

        composable(navCommand = NavCommand.ContentWebView(Feature.PODCAST)) {
            WebViewScreen(
                url = it.findArg(NavArg.EncodedUrl),
                onBackClicked = {
                    navController.goBack()
                },
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
        startDestination = NavCommand.ContentType(Feature.PREMIUM_AUDIOS).route,
        route = Feature.PREMIUM_AUDIOS.route,
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PREMIUM_AUDIOS)) {
            appState.setStartDestination(Feature.PREMIUM_AUDIOS)
            PremiumAudiosScreenRoot(
                onRequireAuth = onRequireAuth,
                onItemClicked = { premiumAudioId ->
                    navController.navigateToDetailFromPremium(premiumAudioId)
                },
                onPlaylistClicked = {
                    navController.navigate(
                        route = NavCommand.ContentType(Feature.PLAYLISTS).route,
                    )
                },
                onAddToPlaylistClicked = { audioItemId ->
                    navController.navigateToPlaylistAudioItem(audioItemId = audioItemId)
                },
            )
        }

        val command =
            NavCommand.ContentDetail(Feature.PREMIUM_AUDIOS, listOf(NavArg.PremiumAudioId))
        composable(
            route = command.route,
            arguments = command.arguments,
            deepLinks = command.deepLinks,
        ) {
            DetailScreen(
                audioId = it.findArg<String>(NavArg.PremiumAudioId),
                Feature.PREMIUM_AUDIOS,
                onBackClicked = {
                    navController.goBack()
                },
                onAddToPlaylistClicked = { audioItemId ->
                    navController.navigateToPlaylistAudioItem(audioItemId = audioItemId)
                },
            )
        }
    }
}

private fun NavGraphBuilder.audioCoursesNav(
    navController: NavController,
    appState: AppState,
) {
    navigation(
        startDestination = NavCommand.ContentType(Feature.AUDIO_COURSES).route,
        route = Feature.AUDIO_COURSES.route,
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.AUDIO_COURSES)) {
            appState.setStartDestination(Feature.AUDIO_COURSES)
            AudioCoursesScreenRoot(
                onItemClicked = { audioCourseId ->
                    navController.navigateToAudioCourseDetailFromAudiocourses(audioCourseId)
                },
                onPlaylistClicked = {
                    navController.navigate(
                        route = NavCommand.ContentType(Feature.PLAYLISTS).route,
                    )
                },
            )
        }
        val command =
            NavCommand.ContentCoursesDetail(Feature.AUDIO_COURSES, listOf(NavArg.AudioCourseId))
        composable(
            route = command.route,
            arguments = command.arguments,
            deepLinks = command.deepLinks,
        ) {
            AudioCoursesDetailScreenRoot(
                audioCourseId = it.findArg<String>(NavArg.AudioCourseId),
                onBackClicked = {
                    navController.goBack()
                },
                onAddToPlaylistClicked = { audioItemId ->
                    navController.navigateToPlaylistAudioItem(audioItemId = audioItemId)
                },
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
        route = Feature.PROFILE.route,
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PROFILE)) {
            appState.setStartDestination(Feature.PROFILE)
            ProfileScreenRoot(
                onPlaylistClicked = {
                    navController.navigate(
                        route = NavCommand.ContentType(Feature.PLAYLISTS).route,
                    )
                },
                onToggleBottomSheet = onRequireAuth,
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
        route = Feature.PLAYLISTS.route,
    ) {
        composable(navCommand = NavCommand.ContentType(Feature.PLAYLISTS)) {
            appState.setStartDestination(Feature.PLAYLISTS)
            PlaylistScreenRoot(
                onBackClicked = {
                    navController.goBackOrNavigateTo(Feature.PROFILE.route)
                },
                onItemClick = { playlistId ->
                    navController.navigateToPlaylistDetailFromPlaylist(playlistId)
                },
            )
        }
        composable(
            navCommand =
                NavCommand.ContentPlaylistDetail(
                    Feature.PLAYLISTS,
                    listOf(NavArg.PlaylistId),
                ),
        ) {
            PlaylistDetailScreenRoot(
                playlistId = it.findArg(NavArg.PlaylistId),
                onBackClicked = {
                    navController.goBack()
                },
            )
        }
        composable(
            navCommand =
                NavCommand.ContentAudioItemDetail(
                    Feature.PLAYLISTS,
                    listOf(NavArg.AudioItemId),
                ),
        ) {
            PlaylistAudioItemRoot(
                playlistAudioId = it.findArg(NavArg.AudioItemId),
                onBackClicked = {
                    navController.goBack()
                },
                onNewPlaylistClicked = {
                    navController.navigate(
                        route = NavCommand.ContentType(Feature.PLAYLISTS).route,
                    )
                },
            )
        }
    }
}

private fun NavGraphBuilder.composable(
    navCommand: NavCommand,
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = navCommand.route,
        arguments = navCommand.args,
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
