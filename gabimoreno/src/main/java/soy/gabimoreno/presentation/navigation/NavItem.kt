package soy.gabimoreno.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.compose.navArgument

sealed class NavItem(
    internal val feature: Feature,
    internal val subRoute: String = "home",
    private val navArgs: List<NavArg> = emptyList()
) {
    class ContentType(feature: Feature) : NavItem(feature)

    class ContentDetail(feature: Feature) : NavItem(
        feature,
        "detail",
        listOf(NavArg.EpisodeId)
    ) {
        fun createRoute(episodeId: String) = "${feature.route}/$subRoute/$episodeId"
    }

    class ContentWebView(feature: Feature) : NavItem(
        feature,
        "webView",
        listOf(NavArg.EncodedUrl)
    ) {
        fun createRoute(encodedUrl: String) = "${feature.route}/$subRoute/$encodedUrl"
    }

    val route = run {
        val argValues = navArgs.map { "{${it.key}}" }
        listOf(feature.route, subRoute)
            .plus(argValues)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }
}

enum class NavArg(
    val key: String,
    val navType: NavType<*>
) {
    EpisodeId("episodeId", NavType.StringType),
    EncodedUrl("encodedUrl", NavType.StringType)
}
