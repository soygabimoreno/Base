package soy.gabimoreno.presentation.navigation

import androidx.navigation.NavController

internal fun NavController.navigateToDetailFromPodcast(episodeId: String) {
    navigate(
        route =
            NavCommand
                .ContentDetail(Feature.PODCAST, listOf(NavArg.EpisodeId))
                .createRoute(episodeId),
    )
}

internal fun NavController.navigateToDetailFromPremium(premiumAudioId: String) {
    navigate(
        route =
            NavCommand
                .ContentDetail(Feature.PREMIUM, listOf(NavArg.PremiumAudioId))
                .createRoute(premiumAudioId),
    )
}

internal fun NavController.navigateToAudioCourseDetailFromAudiocourses(audioCourseId: String) {
    navigate(
        route =
            NavCommand
                .ContentCoursesDetail(Feature.AUDIOCOURSES, listOf(NavArg.AudioCourseId))
                .createRoute(audioCourseId),
    )
}

internal fun NavController.navigateToPlaylistDetailFromPlaylist(playlistId: String) {
    navigate(
        route =
            NavCommand
                .ContentPlaylistDetail(Feature.PLAYLISTS, listOf(NavArg.PlaylistId))
                .createRoute(playlistId),
    )
}

internal fun NavController.navigateToPlaylistAudioItem(audioItemId: String) {
    navigate(
        route =
            NavCommand
                .ContentAudioItemDetail(Feature.PLAYLISTS, listOf(NavArg.AudioItemId))
                .createRoute(audioItemId),
    )
}

internal fun NavController.navigateToWebView(encodedUrl: String) {
    navigate(route = NavCommand.ContentWebView(Feature.PODCAST).createRoute(encodedUrl))
}

internal fun NavController.goBack() {
    popBackStack()
}

internal fun NavController.goBackOrNavigateTo(route: String) {
    val wasPopped = popBackStack()
    if (!wasPopped) {
        navigate(route) {
            popUpTo(graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}
