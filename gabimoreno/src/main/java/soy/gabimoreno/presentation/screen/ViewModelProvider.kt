package soy.gabimoreno.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import soy.gabimoreno.presentation.screen.detail.DetailViewModel
import soy.gabimoreno.presentation.screen.home.HomeViewModel
import soy.gabimoreno.presentation.screen.player.PlayerViewModel
import soy.gabimoreno.presentation.screen.webview.WebViewViewModel

object ViewModelProvider {
    val homeViewModel: HomeViewModel
        @Composable
        get() = localHomeViewModel.current

    val detailViewModel: DetailViewModel
        @Composable
        get() = localDetailViewModel.current

    val playerViewModel: PlayerViewModel
        @Composable
        get() = localPlayerViewModel.current

    val webViewViewModel: WebViewViewModel
        @Composable
        get() = localWebViewViewModel.current
}

@Composable
fun ProvideMultiViewModel(content: @Composable () -> Unit) {
    val homeViewModel: HomeViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()
    val playerViewModel: PlayerViewModel = viewModel()
    val webViewViewModel: WebViewViewModel = viewModel()

    CompositionLocalProvider(
        localHomeViewModel provides homeViewModel,
        localWebViewViewModel provides webViewViewModel,
    ) {
        CompositionLocalProvider(
            localDetailViewModel provides detailViewModel,
        ) {
            CompositionLocalProvider(
                localPlayerViewModel provides playerViewModel,
            ) {
                content()
            }
        }
    }
}

private val localHomeViewModel = staticCompositionLocalOf<HomeViewModel> {
    error("No HomeViewModel provided")
}

private val localDetailViewModel = staticCompositionLocalOf<DetailViewModel> {
    error("No DetailViewModel provided")
}

private val localPlayerViewModel = staticCompositionLocalOf<PlayerViewModel> {
    error("No PlayerViewModel provided")
}

private val localWebViewViewModel = staticCompositionLocalOf<WebViewViewModel> {
    error("No WebViewViewModel provided")
}
