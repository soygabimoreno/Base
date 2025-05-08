package soy.gabimoreno.presentation.screen.courses.list

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
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.usecase.GetAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.RefreshAudioCoursesUseCase
import javax.inject.Inject

@HiltViewModel
class AudioCoursesListViewModel @Inject constructor(
    private val getCoursesUseCase: GetAudioCoursesUseCase,
    private val refreshAudioCoursesUseCase: RefreshAudioCoursesUseCase,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    var state by mutableStateOf(AudioCoursesListState())
        private set

    private val eventChannel = MutableSharedFlow<AudioCoursesListEvent>()
    val events = eventChannel.asSharedFlow()

    init {
        onViewScreen()
    }

    private fun onViewScreen() {
        state = state.copy(isLoading = true)
        viewModelScope.launch(dispatcher) {
            getCoursesUseCase(categories = listOf(Category.AUDIOCOURSES))
                .onRight { audioCourses ->
                    state = state.copy(
                        isLoading = false,
                        audiocourses = audioCourses
                    )
                }
                .onLeft { throwable: Throwable ->
                    when (throwable) {
                        is TokenExpiredException -> {
                            showTokenExpiredError()
                        }

                        else -> eventChannel.emit(AudioCoursesListEvent.Error(throwable))
                    }
                    state = state.copy(isLoading = false)
                }
        }
    }

    fun onAction(action: AudioCoursesListAction) {
        when (action) {
            is AudioCoursesListAction.OnItemClicked -> {}
            AudioCoursesListAction.OnRefreshContent -> {
                refreshContent()
            }
        }
    }

    private fun showTokenExpiredError() {
        viewModelScope.launch {
            eventChannel.emit(AudioCoursesListEvent.ShowTokenExpiredError)
        }
    }

    private fun refreshContent() {
        viewModelScope.launch(dispatcher) {
            state = state.copy(isRefreshing = true)
            delay(REFRESH_DELAY)
            refreshAudioCoursesUseCase()
            onViewScreen()
            state = state.copy(isRefreshing = false)
        }
    }
}

private const val REFRESH_DELAY = 1500L
