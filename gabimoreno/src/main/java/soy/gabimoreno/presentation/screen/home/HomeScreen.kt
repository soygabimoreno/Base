package soy.gabimoreno.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.core.normalizeText
import soy.gabimoreno.presentation.navigation.deeplink.DeepLinkEpisodeNumber
import soy.gabimoreno.presentation.navigation.deeplink.DeepLinkUrl
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.view.EpisodeView
import soy.gabimoreno.presentation.screen.home.view.ErrorView
import soy.gabimoreno.presentation.screen.home.view.LoadingPlaceholder
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.StaggeredVerticalGrid

@Composable
fun HomeScreen(
    onItemClicked: (episodeId: String) -> Unit,
    onDeepLinkReceived: (episodeId: String) -> Unit,
    onGoToWebClicked: (encodedUrl: String) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val homeViewModel = ViewModelProvider.homeViewModel
    val viewState = homeViewModel.viewState

    LaunchedEffect(Unit) {
        homeViewModel.onViewScreen()
    }

    var searchTextState by remember { mutableStateOf(TextFieldValue("")) }

    Surface {
        Column {
            Text(
                homeViewModel.appVersionName, modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = Spacing.s16, top = Spacing.s4, bottom = Spacing.s4)
            )
            OutlinedTextField(
                value = searchTextState,
                onValueChange = { value ->
                    searchTextState = value
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                modifier = Modifier
                    .padding(start = Spacing.s16, bottom = Spacing.s16, end = Spacing.s16)
                    .fillMaxWidth()
            )
            LazyColumn(state = scrollState) {
                when (viewState) {
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
                                val searchText = searchTextState.text

                                val episodes = viewState.episodesWrapper.episodes
                                val filteredEpisodes = if (searchText.isBlank()) {
                                    episodes
                                } else {
                                    episodes.filter { episode ->
                                        val lowerCaseTitle = episode.title.normalizeText()
                                        val lowerSearchText = searchText.normalizeText()
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
                                // TODO: Manage deep links in a proper way
                                val episodeNumber = DeepLinkEpisodeNumber.value
                                if (episodeNumber != null) {
                                    DeepLinkEpisodeNumber.value = null
                                    val index = filteredEpisodes.size - episodeNumber
                                    val episode = filteredEpisodes[index]
                                    val episodeId = episode.id
                                    homeViewModel.onDeepLinkReceived(episodeId, episode.title)
                                    onDeepLinkReceived(episodeId)
                                } else {
                                    DeepLinkUrl.value?.let { url ->
                                        DeepLinkUrl.value = null
                                        homeViewModel.onShowWebViewClicked(url)
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
                            .padding(bottom = Spacing.s32)
                            .padding(
                                bottom = if (ViewModelProvider.playerViewModel.currentPlayingAudio.value != null) Spacing.s64
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
                is HomeViewModel.ViewEvent.ShowWebView -> {
                    val encodedUrl = viewEvent.encodedUrl
                    if (encodedUrl.isNotBlank()) {
                        onGoToWebClicked(encodedUrl)
                    }
                }
            }
        }
    }
}
