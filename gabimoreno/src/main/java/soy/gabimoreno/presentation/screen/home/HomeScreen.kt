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
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.collect
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL
import soy.gabimoreno.presentation.navigation.EpisodeNumber
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.view.EpisodeView
import soy.gabimoreno.presentation.screen.home.view.ErrorView
import soy.gabimoreno.presentation.screen.home.view.LoadingPlaceholder
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.StaggeredVerticalGrid
import java.util.*

@Composable
fun HomeScreen(
    onItemClicked: (episodeId: String) -> Unit,
    onDeepLinkReceived: (episodeId: String) -> Unit,
    onGoToWebClicked: (encodedUrl: String) -> Unit
) {
    val scrollState = rememberLazyListState()
    val homeViewModel = ViewModelProvider.homeViewModel
    val podcastSearch = homeViewModel.podcastSearch

    val encodedUrl = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        homeViewModel.onViewScreen()
    }

    val searchTextState = remember { mutableStateOf(TextFieldValue("")) }

    Surface {
        Column {
            Text(
                homeViewModel.appVersionName, modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = Spacing.s16, top = Spacing.s4, bottom = Spacing.s4)
            )
            Row(
                modifier = Modifier
                    .padding(start = Spacing.s16, bottom = Spacing.s16, end = Spacing.s16)
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
                    onClick = { homeViewModel.onShowWebViewClicked(GABI_MORENO_WEB_BASE_URL) },
                    modifier = Modifier
                        .padding(start = Spacing.s8, end = Spacing.s8)
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
                                spacing = Spacing.s16,
                                modifier = Modifier.padding(horizontal = Spacing.s16)
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
                                        modifier = Modifier.padding(bottom = Spacing.s16)
                                    ) {
                                        val episodeId = episode.id
                                        homeViewModel.onEpisodeClicked(episodeId, episode.title)
                                        onItemClicked(episodeId)
                                    }
                                }
                                // TODO: Check how to manage deep links
                                val episodeNumber = EpisodeNumber.value
                                if (episodeNumber != null) {
                                    EpisodeNumber.value = null
                                    val index = filteredEpisodes.size - episodeNumber
                                    val episode = filteredEpisodes[index]
                                    val episodeId = episode.id
                                    homeViewModel.onDeepLinkReceived(episodeId, episode.title)
                                    onDeepLinkReceived(episodeId)
                                }
                            }
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(bottom = Spacing.s32)
                            .padding(
                                bottom = if (ViewModelProvider.playerViewModel.currentPlayingEpisode.value != null) Spacing.s64
                                else Spacing.s0
                            )
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.viewEventFlow.collect { viewEvent ->
            when (viewEvent) {
                is HomeViewModel.ViewEvent.ShowWebView -> encodedUrl.value = viewEvent.encodedUrl
            }
        }
    }
    if (encodedUrl.value.isNotBlank()) {
        onGoToWebClicked(encodedUrl.value)
    }
}
