package soy.gabimoreno.presentation.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import com.google.accompanist.insets.navigationBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.core.normalizeText
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.view.EpisodeView
import soy.gabimoreno.presentation.screen.home.view.ErrorView
import soy.gabimoreno.presentation.screen.home.view.LoadingPlaceholder
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.StaggeredVerticalGrid

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onItemClicked: (episodeId: String) -> Unit,
    onDeepLinkReceived: (episodeId: String) -> Unit,
    onGoToWebClicked: (encodedUrl: String) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val homeViewModel = ViewModelProvider.homeViewModel
    val playerViewModel = ViewModelProvider.playerViewModel
    val viewState = homeViewModel.viewState
    var searchTextState by remember { mutableStateOf(TextFieldValue("")) }
    val pullRefreshState =
        rememberPullRefreshState(homeViewModel.isRefreshing, { homeViewModel.pullToRefresh() })
    LaunchedEffect(Unit) {
        homeViewModel.onViewScreen()
    }

    Surface {
        Column {
            Text(
                homeViewModel.appVersionName,
                modifier =
                    Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                        .padding(start = Spacing.s16, bottom = Spacing.s4),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = searchTextState,
                    onValueChange = { value ->
                        searchTextState = value
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search),
                        )
                    },
                    modifier =
                        Modifier
                            .padding(start = Spacing.s16, bottom = Spacing.s16, end = Spacing.s16)
                            .weight(0.95f),
                )
                IconButton(
                    onClick = { homeViewModel.toggleShouldIReversePodcastOrder() },
                    modifier =
                        Modifier
                            .padding(bottom = Spacing.s16, end = Spacing.s16),
                ) {
                    AnimatedContent(
                        targetState = homeViewModel.shouldIReversePodcastOrder,
                        transitionSpec = {
                            scaleIn(tween(300)) togetherWith scaleOut(tween(500))
                        },
                        label = "IconTransition",
                    ) { reversed ->
                        if (reversed) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = stringResource(R.string.podcast_sort_desc),
                                modifier =
                                    Modifier
                                        .rotate(180f)
                                        .size(Spacing.s48),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = stringResource(R.string.podcast_sort_asc),
                                modifier = Modifier.size(Spacing.s48),
                            )
                        }
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState),
            ) {
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

                        is HomeViewModel.ViewState.Success -> {
                            item {
                                StaggeredVerticalGrid(
                                    crossAxisCount = 2,
                                    spacing = Spacing.s16,
                                    modifier = Modifier.padding(horizontal = Spacing.s16),
                                ) {
                                    val searchText = searchTextState.text

                                    val episodes = viewState.episodes
                                    val filteredEpisodes =
                                        if (searchText.isBlank()) {
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
                                            modifier = Modifier.padding(bottom = Spacing.s16),
                                        ) {
                                            val episodeId = episode.id
                                            homeViewModel.onEpisodeClicked(episodeId, episode.title)
                                            onItemClicked(episodeId)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Box(
                            modifier =
                                Modifier
                                    .navigationBarsPadding()
                                    .padding(bottom = Spacing.s32)
                                    .padding(
                                        bottom =
                                            if (playerViewModel.currentPlayingAudio.value != null) {
                                                Spacing.s64
                                            } else {
                                                Spacing.s0
                                            },
                                    ),
                        )
                    }
                }
                PullRefreshIndicator(
                    refreshing = homeViewModel.isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
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
