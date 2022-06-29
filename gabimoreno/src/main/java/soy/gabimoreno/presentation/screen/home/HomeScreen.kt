package soy.gabimoreno.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.presentation.navigation.Destination
import soy.gabimoreno.presentation.navigation.Navigator
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.view.EpisodeView
import soy.gabimoreno.presentation.screen.home.view.ErrorView
import soy.gabimoreno.presentation.screen.home.view.LoadingPlaceholder
import soy.gabimoreno.presentation.ui.PreviewContent
import soy.gabimoreno.presentation.ui.StaggeredVerticalGrid
import soy.gabimoreno.util.Resource

@Composable
fun HomeScreen() {
    val scrollState = rememberLazyListState()
    val navController = Navigator.current
    val homeViewModel = ViewModelProvider.homeViewModel
    val podcastSearch = homeViewModel.podcastSearch

    LaunchedEffect(Unit) {
        homeViewModel.onViewScreen()
    }

    Surface {
        Column {
            Text(
                homeViewModel.appVersionName, modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 16.dp)
            )
            LazyColumn(state = scrollState) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }

                when (podcastSearch) {
                    is Resource.Error -> {
                        item {
                            ErrorView(text = podcastSearch.failure.translate()) {
                                homeViewModel.searchPodcasts()
                            }
                        }
                    }
                    Resource.Loading -> {
                        item {
                            LoadingPlaceholder()
                        }
                    }
                    is Resource.Success -> {
                        item {
                            StaggeredVerticalGrid(
                                crossAxisCount = 2,
                                spacing = 16.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                podcastSearch.data.results.forEach { episode ->
                                    EpisodeView(
                                        episode = episode,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    ) {
                                        homeViewModel.onEpisodeClicked(episode.id)
                                        openDetail(navController, episode)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(bottom = 32.dp)
                            .padding(bottom = if (ViewModelProvider.playerViewModel.currentPlayingEpisode.value != null) 64.dp else 0.dp)
                    )
                }
            }
        }
    }
}

private fun openDetail(
    navController: NavHostController,
    episode: Episode
) {
    navController.navigate(Destination.detail(episode.id)) { }
}

@Composable
@Preview(name = "Home")
fun HomeScreenPreview() {
    PreviewContent {
        HomeScreen()
    }
}

@Composable
@Preview(name = "Home (Dark)")
fun HomeScreenDarkPreview() {
    PreviewContent(darkTheme = true) {
        HomeScreen()
    }
}
