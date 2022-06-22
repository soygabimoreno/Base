package soy.gabimoreno.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.HomeTrackerEvent
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.PodcastSearch
import soy.gabimoreno.domain.repository.PodcastRepository
import soy.gabimoreno.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PodcastRepository,
    private val tracker: Tracker
) : ViewModel() {

    var podcastSearch by mutableStateOf<Resource<PodcastSearch>>(Resource.Loading)
        private set

    init {
        tracker.trackEvent(HomeTrackerEvent.ScreenHome(mapOf("ExampleKey" to "ExampleValue")))
        searchPodcasts()
    }

    fun getPodcastDetail(id: String): Episode? {
        return when (podcastSearch) {
            is Resource.Error -> null
            Resource.Loading -> null
            is Resource.Success -> (podcastSearch as Resource.Success<PodcastSearch>).data.results.find { it.id == id }
        }
    }

    fun searchPodcasts() {
        viewModelScope.launch {
            podcastSearch = Resource.Loading
            val result = repository.getPodcast()
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

    fun onEpisodeClicked(id: String) {
        tracker.trackEvent(HomeTrackerEvent.ClickEpisode(mapOf("id" to id)))
    }
}
