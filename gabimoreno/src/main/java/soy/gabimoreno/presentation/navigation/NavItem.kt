package soy.gabimoreno.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import soy.gabimoreno.R

enum class NavItem(
    val navCommand: NavCommand,
    val icon: ImageVector,
    @StringRes val titleResId: Int
) {
    PODCAST(
        navCommand = NavCommand.ContentType(Feature.PODCAST),
        icon = Icons.Default.Podcasts,
        titleResId = R.string.nav_item_podcast
    ),
    PREMIUM(
        navCommand = NavCommand.ContentType(Feature.PREMIUM),
        icon = Icons.Default.WorkspacePremium,
        titleResId = R.string.nav_item_premium
    )
}

sealed class NavCommand(
    internal val feature: Feature,
    internal val subRoute: String = "home",
    private val navArgs: List<NavArg> = emptyList()
) {
    class ContentType(feature: Feature) : NavCommand(feature)

    class ContentDetail(feature: Feature) : NavCommand(
        feature,
        "detail",
        listOf(NavArg.EpisodeId)
    ) {
        fun createRoute(episodeId: String) = "${feature.route}/$subRoute/$episodeId"
    }

    class ContentWebView(feature: Feature) : NavCommand(
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
