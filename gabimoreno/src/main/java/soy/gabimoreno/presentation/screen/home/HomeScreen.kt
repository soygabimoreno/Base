package soy.gabimoreno.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.collect
import soy.gabimoreno.R
import soy.gabimoreno.presentation.navigation.Destination
import soy.gabimoreno.presentation.navigation.EpisodeNumber
import soy.gabimoreno.presentation.navigation.Navigator
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.view.EpisodeView
import soy.gabimoreno.presentation.screen.home.view.ErrorView
import soy.gabimoreno.presentation.screen.home.view.LoadingPlaceholder
import soy.gabimoreno.presentation.ui.StaggeredVerticalGrid
import java.util.*

@Composable
fun HomeScreen() {
    val scrollState = rememberLazyListState()
    val navController = Navigator.current
    val homeViewModel = ViewModelProvider.homeViewModel
    val podcastSearch = homeViewModel.podcastSearch

    val webViewUrl = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        homeViewModel.onViewScreen()
    }

    val searchTextState = remember { mutableStateOf(TextFieldValue("")) }

    Surface {
        Column {
            Text(
                homeViewModel.appVersionName, modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = searchTextState.value,
                    onValueChange = { value ->
                        searchTextState.value = value
                    },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
                TextButton(
                    onClick = { homeViewModel.onShowWebViewClicked("https://gabimoreno.soy") },
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.go_to_the_web),
                        textAlign = TextAlign.Center
                    )
                }
            }
            LazyColumn(state = scrollState) {
                when (podcastSearch) {
                    is HomeViewModel.ViewState.Error -> {
                        item {
                            ErrorView(text = stringResource(R.string.unexpected_error)) {
                                homeViewModel.searchPodcasts()
                            }
                        }
                    }
                    HomeViewModel.ViewState.Loading -> {
                        item {
                            LoadingPlaceholder()
                        }
                    }
                    is HomeViewModel.ViewState.Content -> {
                        item {
                            StaggeredVerticalGrid(
                                crossAxisCount = 2,
                                spacing = 16.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                val searchText = searchTextState.value.text
                                val episodes = podcastSearch.data.results
                                val filteredEpisodes = if (searchText.isBlank()) {
                                    episodes
                                } else {
                                    episodes.filter { episode ->
                                        val lowerCaseTitle = episode.title.lowercase(Locale.US)
                                        val lowerSearchText = searchText.lowercase(Locale.US)
                                        lowerCaseTitle.contains(lowerSearchText)
                                    }
                                }
                                filteredEpisodes.forEach { episode ->
                                    EpisodeView(
                                        episode = episode,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    ) {
                                        homeViewModel.onEpisodeClicked(episode.id, episode.title)
                                        openDetail(navController, episode.id)
                                    }
                                }
                                // TODO: Check how to manage deep links
                                val episodeNumber = EpisodeNumber.value
                                if (episodeNumber != null) {
                                    EpisodeNumber.value = null
                                    val index = filteredEpisodes.size - episodeNumber
                                    val episode = filteredEpisodes[index]
                                    val episodeId = episode.id
                                    openDetail(navController, episodeId)
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

    LaunchedEffect(Unit) {
        homeViewModel.viewEventFlow.collect { viewEvent ->
            when (viewEvent) {
                is HomeViewModel.ViewEvent.ShowWebView -> webViewUrl.value = viewEvent.url
            }
        }
    }
    if (webViewUrl.value.isNotBlank()) {
        navController.navigate(Destination.webView(webViewUrl.value)) { }
    }
}

private fun openDetail(
    navController: NavHostController,
    episodeId: String
) {
    navController.navigate(Destination.detail(episodeId)) { }
}
