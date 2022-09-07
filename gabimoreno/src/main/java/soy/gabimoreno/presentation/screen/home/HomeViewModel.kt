package soy.gabimoreno.presentation.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EPISODE_ID
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EPISODE_TITLE
import soy.gabimoreno.data.tracker.main.HomeTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.PodcastSearch
import soy.gabimoreno.domain.repository.PodcastRepository
import soy.gabimoreno.domain.usecase.EncodeUrlUseCase
import soy.gabimoreno.domain.usecase.GetAppVersionNameUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val podcastRepository: PodcastRepository,
    private val tracker: Tracker,
    getAppVersionNameUseCase: GetAppVersionNameUseCase,
    private val encodeUrlUseCase: EncodeUrlUseCase,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    var podcastSearch by mutableStateOf<ViewState>(ViewState.Loading)
        private set

    var appVersionName by mutableStateOf("")

    private val _viewEventFlow = MutableSharedFlow<ViewEvent>()
    val viewEventFlow = _viewEventFlow.asSharedFlow()

    init {
        appVersionName = getAppVersionNameUseCase()
        searchPodcasts()
    }

    fun searchPodcasts() {
        viewModelScope.launch(dispatcher) {
            podcastSearch = ViewState.Loading
            podcastRepository.getEpisodes().fold(
                { failure ->
                    podcastSearch = ViewState.Error(failure)
                },
                { data ->
                    podcastSearch = ViewState.Content(data)
                }
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
                )
            )
        )
    }

    fun onDeepLinkReceived(
        episodeId: String,
        episodeTitle: String,
    ) {
        tracker.trackEvent(
            HomeTrackerEvent.ReceiveDeepLink(
                mapOf(
                    TRACKER_KEY_EPISODE_ID to episodeId,
                    TRACKER_KEY_EPISODE_TITLE to episodeTitle,
                )
            )
        )
    }

    fun getPodcastDetail(id: String): Episode? {
        return when (podcastSearch) {
            is ViewState.Content -> (podcastSearch as ViewState.Content).data.results.find { it.id == id }
            else -> null
        }
    }

    sealed class ViewEvent {
        data class ShowWebView(val encodedUrl: String) : ViewEvent()
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Error(val throwable: Throwable) : ViewState()
        data class Content(val data: PodcastSearch) : ViewState()
    }
}
