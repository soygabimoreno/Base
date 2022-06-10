package soy.gabimoreno.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import soy.gabimoreno.presentation.viewmodel.PodcastDetailViewModel
import soy.gabimoreno.presentation.viewmodel.PodcastPlayerViewModel
import soy.gabimoreno.presentation.viewmodel.PodcastSearchViewModel

object ViewModelProvider {
    val podcastSearch: PodcastSearchViewModel
        @Composable
        get() = LocalPodcastSearchViewModel.current

    val podcastDetail: PodcastDetailViewModel
        @Composable
        get() = LocalPodcastDetailViewModel.current

    val podcastPlayer: PodcastPlayerViewModel
        @Composable
        get() = LocalPodcastPlayerViewModel.current
}

@Composable
fun ProvideMultiViewModel(content: @Composable () -> Unit) {
    val podcastSearchViewModel: PodcastSearchViewModel = viewModel()
    val podcastDetailViewModel: PodcastDetailViewModel = viewModel()
    val podcastPlayerViewModel: PodcastPlayerViewModel = viewModel()

    CompositionLocalProvider(
        LocalPodcastSearchViewModel provides podcastSearchViewModel,
    ) {
        CompositionLocalProvider(
            LocalPodcastDetailViewModel provides podcastDetailViewModel,
        ) {
            CompositionLocalProvider(
                LocalPodcastPlayerViewModel provides podcastPlayerViewModel,
            ) {
                content()
            }
        }
    }
}

private val LocalPodcastSearchViewModel = staticCompositionLocalOf<PodcastSearchViewModel> {
    error("No PodcastSearchViewModel provided")
}

private val LocalPodcastDetailViewModel = staticCompositionLocalOf<PodcastDetailViewModel> {
    error("No PodcastDetailViewModel provided")
}

private val LocalPodcastPlayerViewModel = staticCompositionLocalOf<PodcastPlayerViewModel> {
    error("No PodcastPlayerViewModel provided")
}
