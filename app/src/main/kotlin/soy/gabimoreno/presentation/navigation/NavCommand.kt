package soy.gabimoreno.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL

sealed class NavCommand(
    internal val feature: Feature,
    internal val subRoute: String = "home",
    private val navArgs: List<NavArg> = emptyList(),
) {
    val route: String
        get() =
            buildString {
                append("${feature.route}/$subRoute")
                navArgs.forEach { append("/{${it.key}}") }
            }

    val arguments: List<NamedNavArgument>
        get() =
            navArgs.map {
                navArgument(it.key) { type = it.navType }
            }

    val deepLinks: List<NavDeepLink>
        get() =
            listOf(
                navDeepLink {
                    uriPattern = "$GABI_MORENO_WEB_BASE_URL/$route"
                },
            )

    class ContentType(
        feature: Feature,
    ) : NavCommand(feature)

    class ContentDetail(
        feature: Feature,
        navArgs: List<NavArg>,
    ) : NavCommand(
            feature = feature,
            subRoute = "detail",
            navArgs = navArgs,
        ) {
        fun createRoute(audioId: String) = "${feature.route}/$subRoute/$audioId"
    }

    class ContentCoursesDetail(
        feature: Feature,
        navArgs: List<NavArg>,
    ) : NavCommand(
            feature = feature,
            subRoute = "audiocoursedetail",
            navArgs = navArgs,
        ) {
        fun createRoute(audioId: String) = "${feature.route}/$subRoute/$audioId"
    }

    class ContentPlaylistDetail(
        feature: Feature,
        navArgs: List<NavArg>,
    ) : NavCommand(
            feature = feature,
            subRoute = "playlistdetail",
            navArgs = navArgs,
        ) {
        fun createRoute(playlistId: String) = "${feature.route}/$subRoute/$playlistId"
    }

    class ContentAudioItemDetail(
        feature: Feature,
        navArgs: List<NavArg>,
    ) : NavCommand(
            feature = feature,
            subRoute = "audioItemDetail",
            navArgs = navArgs,
        ) {
        fun createRoute(audioItemId: String) = "${feature.route}/$subRoute/$audioItemId"
    }

    class ContentWebView(
        feature: Feature,
    ) : NavCommand(
            feature = feature,
            subRoute = "webView",
            navArgs = listOf(NavArg.EncodedUrl),
        ) {
        fun createRoute(encodedUrl: String) = "${feature.route}/$subRoute/$encodedUrl"
    }

    val args =
        navArgs.map {
            navArgument(it.key) { type = it.navType }
        }
}
