package soy.gabimoreno.presentation.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EPISODE_ID
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EPISODE_TITLE
import soy.gabimoreno.data.tracker.main.HomeTrackerEvent
import soy.gabimoreno.di.Main
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.usecase.HomeUseCases
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val homeUseCases: HomeUseCases,
        private val tracker: Tracker,
        @param:Main private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        var viewState by mutableStateOf<ViewState>(ViewState.Loading)
            private set
        private val episodes = mutableListOf<Episode>()

        var shouldIReversePodcastOrder by mutableStateOf(false)
            private set

        var appVersionName by mutableStateOf("")

        var isRefreshing by mutableStateOf(false)
            private set

        private val _viewEventFlow = MutableSharedFlow<ViewEvent>()
        val viewEventFlow = _viewEventFlow.asSharedFlow()

        init {
            appVersionName = homeUseCases.getAppVersionName()
            searchPodcasts()
            viewModelScope.launch(dispatcher) {
                shouldIReversePodcastOrder = homeUseCases.getShouldIReversePodcastOrder()
            }
        }

        fun searchPodcasts() {
            viewModelScope.launch(dispatcher) {
                viewState = ViewState.Loading
                homeUseCases.getPodcastStreamUseCase().fold(
                    { failure ->
                        viewState = ViewState.Error(failure)
                    },
                    { episodesFlow ->
                        episodesFlow
                            .distinctUntilChanged()
                            .collect { incomingEpisodes ->
                                episodes.clear()
                                episodes.addAll(incomingEpisodes)

                                if (incomingEpisodes.isNotEmpty()) {
                                    val lastTitle = incomingEpisodes.last().title
                                    val isFirstPodcast = Regex("^1\\..*")
                                    if (isFirstPodcast.matches(lastTitle) &&
                                        shouldIReversePodcastOrder
                                    ) {
                                        episodes.reverse()
                                    }
                                }

                                viewState = ViewState.Success(episodes.toList())
                            }
                    },
                )
            }
        }

        fun onViewScreen() {
            tracker.trackEvent(HomeTrackerEvent.ViewScreen)
        }

        fun onShowWebViewClicked(url: String) {
            viewModelScope.launch(dispatcher) {
                _viewEventFlow.emit(
                    ViewEvent.ShowWebView(homeUseCases.encodeUrl(url)),
                )
            }
        }

        fun onEpisodeClicked(
            episodeId: String,
            episodeTitle: String,
        ) {
            tracker.trackEvent(
                HomeTrackerEvent.ClickEpisode(
                    mapOf(
                        TRACKER_KEY_EPISODE_ID to episodeId,
                        TRACKER_KEY_EPISODE_TITLE to episodeTitle,
                    ),
                ),
            )
        }

        fun pullToRefresh() {
            viewModelScope.launch(dispatcher) {
                isRefreshing = true
                searchPodcasts()
                delay(REFRESH_DELAY)
                isRefreshing = false
            }
        }

        fun findEpisodeFromId(id: String): Episode? =
            when (viewState) {
                is ViewState.Success ->
                    (viewState as ViewState.Success).episodes.find {
                        it.id == id
                    }

                else -> null
            }

        fun toggleShouldIReversePodcastOrder() {
            shouldIReversePodcastOrder = !shouldIReversePodcastOrder
            viewState = ViewState.Loading
            viewModelScope.launch(dispatcher) {
                homeUseCases.setShouldIReversePodcastOrder(shouldIReversePodcastOrder)
            }
            episodes.reverse()
            viewState = ViewState.Success(episodes.toList())
        }

        fun onFavoriteStatusChanged(episode: Episode) {
            viewModelScope.launch(dispatcher) {
                homeUseCases
                    .updateAudioItemFavoriteStateUseCase(episode.id, !episode.markedAsFavorite)
            }
            val episodeUpdated =
                episodes.map {
                    if (it.id == episode.id) {
                        it.copy(markedAsFavorite = !episode.markedAsFavorite)
                    } else {
                        it
                    }
                }
            episodes.clear()
            episodes.addAll(episodeUpdated)
        }

        fun onListenedToggled(episode: Episode) {
            viewModelScope.launch(dispatcher) {
                homeUseCases
                    .markPodcastAsListenedUseCase(episode.id, !episode.hasBeenListened)
            }
            val episodeUpdated =
                episodes.map {
                    if (it.id == episode.id) {
                        it.copy(hasBeenListened = !episode.hasBeenListened)
                    } else {
                        it
                    }
                }
            episodes.clear()
            episodes.addAll(episodeUpdated)
        }

        sealed class ViewEvent {
            data class ShowWebView(
                val encodedUrl: String,
            ) : ViewEvent()
        }

        sealed class ViewState {
            data object Loading : ViewState()
            data class Error(
                val throwable: Throwable,
            ) : ViewState()

            data class Success(
                val episodes: List<Episode>,
            ) : ViewState()
        }
    }

private const val REFRESH_DELAY = 1_500L
