package soy.gabimoreno.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object Navigator {
    val current: NavHostController
        @Composable
        get() = localNavHostController.current
}

@Composable
fun ProvideNavHostController(content: @Composable () -> Unit) {
    val navController = rememberNavController()
    CompositionLocalProvider(
        localNavHostController provides navController,
        content = content
    )
}

private val localNavHostController = staticCompositionLocalOf<NavHostController> {
    error("No NavHostController provided")
}

object EpisodeNumber {
    var value: Int? = null
}
