package soy.gabimoreno.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.ui.common.PreviewContent
import soy.gabimoreno.ui.common.StaggeredVerticalGrid
import soy.gabimoreno.ui.common.ViewModelProvider
import soy.gabimoreno.ui.navigation.Destination
import soy.gabimoreno.ui.navigation.Navigator
import soy.gabimoreno.util.Resource

@Composable
fun HomeScreen() {
    val scrollState = rememberLazyListState()
    val navController = Navigator.current
    val homeViewModel = ViewModelProvider.homeViewModel
    val podcastSearch = homeViewModel.podcastSearch

    Surface {
        LazyColumn(state = scrollState) {
            item {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
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
                            podcastSearch.data.results.forEach { podcast ->
                                EpisodeView(
                                    podcast = podcast,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    homeViewModel.onEpisodeClicked(podcast.id)
                                    openPodcastDetail(navController, podcast)
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

private fun openPodcastDetail(
    navController: NavHostController,
    podcast: Episode
) {
    navController.navigate(Destination.podcast(podcast.id)) { }
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
