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
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material.pullrefresh.PullRefreshState
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
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.view.EpisodeView
import soy.gabimoreno.presentation.screen.home.view.ErrorView
import soy.gabimoreno.presentation.screen.home.view.LoadingPlaceholder
import soy.gabimoreno.presentation.theme.Percent
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.StaggeredVerticalGrid

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onItemClicked: (episodeId: String) -> Unit,
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
            Header(homeViewModel.appVersionName)
            SearchRow(
                searchText = searchTextState,
                onSearchTextChanged = { searchTextState = it },
                onToggleOrder = { homeViewModel.toggleShouldIReversePodcastOrder() },
                reversed = homeViewModel.shouldIReversePodcastOrder,
            )
            EpisodeListContent(
                viewState = viewState,
                scrollState = scrollState,
                searchText = searchTextState.text,
                isRefreshing = homeViewModel.isRefreshing,
                pullRefreshState = pullRefreshState,
                playerHasAudio = playerViewModel.currentPlayingAudio.value != null,
                onEpisodeClicked = { episode ->
                    homeViewModel.onEpisodeClicked(episode.id, episode.title)
                    onItemClicked(episode.id)
                },
                onRetry = { homeViewModel.searchPodcasts() },
                onFavoriteStatusChanged = { episode ->
                    homeViewModel.onFavoriteStatusChanged(episode)
                },
                onListenedToggled = { episode ->
                    homeViewModel.onListenedToggled(episode)
                },
            )
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.viewEventFlow.collect { viewEvent ->
            if (viewEvent is HomeViewModel.ViewEvent.ShowWebView) {
                val encodedUrl = viewEvent.encodedUrl
                if (encodedUrl.isNotBlank()) {
                    onGoToWebClicked(encodedUrl)
                }
            }
        }
    }
}

@Composable
private fun SearchRow(
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onToggleOrder: () -> Unit,
    reversed: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchTextField(
            searchTextState = searchText,
            onValueChange = onSearchTextChanged,
        )
        IconButton(
            onClick = onToggleOrder,
            modifier = Modifier.padding(bottom = Spacing.s16, end = Spacing.s16),
        ) {
            AnimatedContent(
                targetState = reversed,
                transitionSpec = {
                    scaleIn(tween(SCALE_IN_ANIMATION_DURATION)) togetherWith
                        scaleOut(tween(SCALE_OUT_ANIMATION_DURATION))
                },
                label = "IconTransition",
            ) { isReversed ->
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription =
                        if (isReversed) {
                            stringResource(R.string.podcast_sort_desc)
                        } else {
                            stringResource(R.string.podcast_sort_asc)
                        },
                    modifier =
                        Modifier
                            .rotate(if (isReversed) ROTATE_ANIMATION else 0f)
                            .size(Spacing.s48),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EpisodeListContent(
    viewState: HomeViewModel.ViewState,
    scrollState: LazyListState,
    searchText: String,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    playerHasAudio: Boolean,
    onEpisodeClicked: (Episode) -> Unit,
    onRetry: () -> Unit,
    onFavoriteStatusChanged: (Episode) -> Unit,
    onListenedToggled: (Episode) -> Unit,
) {
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
                            onRetry()
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
                        EpisodeGrid(
                            episodes = viewState.episodes,
                            searchText = searchText,
                            onClick = onEpisodeClicked,
                            onFavoriteStatusChanged = onFavoriteStatusChanged,
                            onListenedToggled = onListenedToggled,
                        )
                    }
                }
            }

            item {
                SpacerWithBottomPadding(playerHasAudio)
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun EpisodeGrid(
    episodes: List<Episode>,
    searchText: String,
    onClick: (Episode) -> Unit,
    onFavoriteStatusChanged: (Episode) -> Unit,
    onListenedToggled: (Episode) -> Unit,
) {
    val filteredEpisodes =
        if (searchText.isBlank()) {
            episodes
        } else {
            val lowerSearch = searchText.normalizeText()
            episodes.filter { it.title.normalizeText().contains(lowerSearch) }
        }

    StaggeredVerticalGrid(
        crossAxisCount = 2,
        spacing = Spacing.s16,
        modifier = Modifier.padding(horizontal = Spacing.s16),
    ) {
        filteredEpisodes.forEach { episode ->
            EpisodeView(
                episode = episode,
                modifier = Modifier.padding(bottom = Spacing.s16),
                onClick = { onClick(episode) },
                onFavoriteStatusChanged = { onFavoriteStatusChanged(episode) },
                onListenedToggled = { onListenedToggled(episode) },
            )
        }
    }
}

@Composable
private fun SpacerWithBottomPadding(playerHasAudio: Boolean) {
    Box(
        modifier =
            Modifier
                .navigationBarsPadding()
                .padding(bottom = Spacing.s32)
                .padding(
                    bottom =
                        if (playerHasAudio) {
                            Spacing.s64
                        } else {
                            Spacing.s0
                        },
                ),
    )
}

@Composable
private fun Header(appVersionName: String) {
    Text(
        appVersionName,
        modifier =
            Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .padding(start = Spacing.s16, bottom = Spacing.s4),
    )
}

@Composable
private fun RowScope.SearchTextField(
    searchTextState: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    OutlinedTextField(
        value = searchTextState,
        onValueChange = { onValueChange(it) },
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
                .weight(Percent.NINETY_FIVE),
    )
}

private const val ROTATE_ANIMATION = 180f
private const val SCALE_IN_ANIMATION_DURATION = 300
private const val SCALE_OUT_ANIMATION_DURATION = 500
