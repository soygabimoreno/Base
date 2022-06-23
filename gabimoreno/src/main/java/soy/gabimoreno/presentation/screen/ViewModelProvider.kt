package soy.gabimoreno.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import soy.gabimoreno.presentation.screen.detail.DetailViewModel
import soy.gabimoreno.presentation.screen.home.HomeViewModel
import soy.gabimoreno.presentation.screen.player.PlayerViewModel

object ViewModelProvider {
    val homeViewModel: HomeViewModel
        @Composable
        get() = LocalHomeViewModel.current

    val detailViewModel: DetailViewModel
        @Composable
        get() = LocalDetailViewModel.current

    val playerViewModel: PlayerViewModel
        @Composable
        get() = LocalPlayerViewModel.current
}

@Composable
fun ProvideMultiViewModel(content: @Composable () -> Unit) {
    val homeViewModel: HomeViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()
    val playerViewModel: PlayerViewModel = viewModel()

    CompositionLocalProvider(
        LocalHomeViewModel provides homeViewModel,
    ) {
        CompositionLocalProvider(
            LocalDetailViewModel provides detailViewModel,
        ) {
            CompositionLocalProvider(
                LocalPlayerViewModel provides playerViewModel,
            ) {
                content()
            }
        }
    }
}

private val LocalHomeViewModel = staticCompositionLocalOf<HomeViewModel> {
    error("No PodcastSearchViewModel provided")
}

private val LocalDetailViewModel = staticCompositionLocalOf<DetailViewModel> {
    error("No PodcastDetailViewModel provided")
}

private val LocalPlayerViewModel = staticCompositionLocalOf<PlayerViewModel> {
    error("No PodcastPlayerViewModel provided")
}
