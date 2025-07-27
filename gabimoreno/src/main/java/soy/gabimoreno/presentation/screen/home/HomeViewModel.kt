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
import kotlinx.coroutines.launch
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EPISODE_ID
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EPISODE_TITLE
import soy.gabimoreno.data.tracker.main.HomeTrackerEvent
import soy.gabimoreno.di.Main
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.usecase.EncodeUrlUseCase
import soy.gabimoreno.domain.usecase.GetAppVersionNameUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReversePodcastOrderUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReversePodcastOrderUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val podcastDatasource: PodcastRepository,
        private val tracker: Tracker,
        getAppVersionNameUseCase: GetAppVersionNameUseCase,
        private val encodeUrlUseCase: EncodeUrlUseCase,
        private val getShouldIReversePodcastOrderUseCase: GetShouldIReversePodcastOrderUseCase,
        private val setShouldIReversePodcastOrderUseCase: SetShouldIReversePodcastOrderUseCase,
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
            appVersionName = getAppVersionNameUseCase()
            searchPodcasts()
            viewModelScope.launch(dispatcher) {
                shouldIReversePodcastOrder = getShouldIReversePodcastOrderUseCase()
            }
        }

        fun searchPodcasts() {
            viewModelScope.launch(dispatcher) {
                viewState = ViewState.Loading
                podcastDatasource.getEpisodesStream().fold(
                    { failure ->
                        viewState = ViewState.Error(failure)
                    },
                    { episodesFlow ->
                        episodesFlow.collect { incomingEpisodes ->
                            if (episodes.size != incomingEpisodes.size) {
                                episodes.clear()
                                episodes.addAll(incomingEpisodes)
                                viewState = ViewState.Success(episodes.toList())
                            }
                            val isFirstPodcast = Regex("^1\\..*")
                            if (isFirstPodcast.matches(incomingEpisodes.last().title) &&
                                shouldIReversePodcastOrder
                            ) {
                                episodes.reverse()
                                viewState = ViewState.Success(episodes.toList())
                            }
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
                _viewEventFlow.emit(ViewEvent.ShowWebView(encodeUrlUseCase(url)))
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

//        fun onDeepLinkReceived(
//            episodeId: String,
//            episodeTitle: String,
//        ) {
//            tracker.trackEvent(
//                HomeTrackerEvent.ReceiveDeepLink(
//                    mapOf(
//                        TRACKER_KEY_EPISODE_ID to episodeId,
//                        TRACKER_KEY_EPISODE_TITLE to episodeTitle,
//                    ),
//                ),
//            )
//        }

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
                setShouldIReversePodcastOrderUseCase(shouldIReversePodcastOrder)
            }
            episodes.reverse()
            viewState = ViewState.Success(episodes.toList())
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
//        data class Content(val episodesWrapper: EpisodesWrapper) : ViewState()
        }
    }

private const val REFRESH_DELAY = 1_500L
