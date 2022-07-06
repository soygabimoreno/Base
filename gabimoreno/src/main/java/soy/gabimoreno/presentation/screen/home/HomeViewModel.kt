package soy.gabimoreno.presentation.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.EPISODE_ID
import soy.gabimoreno.data.tracker.domain.EPISODE_TITLE
import soy.gabimoreno.data.tracker.main.HomeTrackerEvent.ClickEpisode
import soy.gabimoreno.data.tracker.main.HomeTrackerEvent.ViewScreen
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.PodcastSearch
import soy.gabimoreno.domain.repository.PodcastRepository
import soy.gabimoreno.domain.usecase.GetAppVersionNameUseCase
import soy.gabimoreno.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val podcastRepository: PodcastRepository,
    private val tracker: Tracker,
    getAppVersionNameUseCase: GetAppVersionNameUseCase,
    @IO private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    var podcastSearch by mutableStateOf<Resource<PodcastSearch>>(Resource.Loading)
        private set

    var appVersionName by mutableStateOf("")

    init {
        appVersionName = getAppVersionNameUseCase()
        searchPodcasts()
    }

    fun searchPodcasts() {
        viewModelScope.launch(dispatcher) {
            podcastSearch = Resource.Loading
            val result = podcastRepository.getEpisodes()
            result.fold(
                { failure ->
                    podcastSearch = Resource.Error(failure)
                },
                { data ->
                    podcastSearch = Resource.Success(data)
                }
            )
        }
    }

    fun onViewScreen() {
        tracker.trackEvent(ViewScreen)
    }

    fun onEpisodeClicked(
        episodeId: String,
        episodeTitle: String
    ) {
        tracker.trackEvent(
            ClickEpisode(
                mapOf(
                    EPISODE_ID to episodeId,
                    EPISODE_TITLE to episodeTitle,
                )
            )
        )
    }

    fun getPodcastDetail(id: String): Episode? {
        return when (podcastSearch) {
            is Resource.Error -> null
            Resource.Loading -> null
            is Resource.Success -> (podcastSearch as Resource.Success<PodcastSearch>).data.results.find { it.id == id }
        }
    }
}
