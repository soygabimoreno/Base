package soy.gabimoreno.presentation.screen.senioraudios

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
import soy.gabimoreno.data.tracker.main.SeniorTrackerEvent
import soy.gabimoreno.di.Main
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.usecase.GetAppVersionNameUseCase
import soy.gabimoreno.domain.usecase.GetSeniorAudioStreamUseCase
import javax.inject.Inject

@HiltViewModel
class SeniorAudiosViewModel
    @Inject
    constructor(
        private val getSeniorAudioStreamUseCase: GetSeniorAudioStreamUseCase,
        private val tracker: Tracker,
        val getAppVersionNameUseCase: GetAppVersionNameUseCase,
        @param:Main private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        var viewState by mutableStateOf<ViewState>(ViewState.Loading)
            private set
        private val episodes = mutableListOf<Episode>()

        var isRefreshing by mutableStateOf(false)
            private set

        private val _viewEventFlow = MutableSharedFlow<ViewEvent>()
        val viewEventFlow = _viewEventFlow.asSharedFlow()

        var appVersionName by mutableStateOf("")

        init {
            appVersionName = getAppVersionNameUseCase()
            searchPodcasts()
        }

        fun searchPodcasts() {
            viewModelScope.launch(dispatcher) {
                viewState = ViewState.Loading
                getSeniorAudioStreamUseCase().fold(
                    { failure ->
                        viewState = ViewState.Error(failure)
                    },
                    { episodesFlow ->
                        episodesFlow
                            .distinctUntilChanged()
                            .collect { incomingEpisodes ->
                                episodes.clear()
                                episodes.addAll(incomingEpisodes)
                                viewState = ViewState.Success(episodes.toList())
                            }
                    },
                )
            }
        }

        fun onViewScreen() {
            tracker.trackEvent(SeniorTrackerEvent.ViewScreen)
        }

        fun onEpisodeClicked(
            episodeId: String,
            episodeTitle: String,
        ) {
            tracker.trackEvent(
                SeniorTrackerEvent.ClickEpisode(
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
                is ViewState.Success -> {
                    (viewState as ViewState.Success).episodes.find {
                        it.id == id
                    }
                }

                else -> {
                    null
                }
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
