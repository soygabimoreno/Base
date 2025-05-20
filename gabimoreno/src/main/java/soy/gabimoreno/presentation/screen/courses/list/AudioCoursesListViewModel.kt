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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.usecase.GetAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import javax.inject.Inject

@HiltViewModel
class AudioCoursesListViewModel @Inject constructor(
    private val getCoursesUseCase: GetAudioCoursesUseCase,
    private val getShouldIReloadAudioCoursesUseCase: GetShouldIReloadAudioCoursesUseCase,
    private val setShouldIReloadAudioCoursesUseCase: SetShouldIReloadAudioCoursesUseCase,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    var state by mutableStateOf(AudioCoursesListState())
        private set

    private val eventChannel = MutableSharedFlow<AudioCoursesListEvent>()
    val events = eventChannel.asSharedFlow()

    init {
        viewModelScope.launch(dispatcher) {
            getShouldIReloadAudioCoursesUseCase()
                .distinctUntilChanged()
                .collect { shouldIReload ->
                    if (shouldIReload) {
                        setShouldIReloadAudioCoursesUseCase(false)
                        onViewScreen(forceRefresh = true)
                    } else {
                        onViewScreen()
                    }
                }
        }
    }

    private fun onViewScreen(forceRefresh: Boolean = false) {
        state = state.copy(isLoading = true)
        viewModelScope.launch(dispatcher) {
            try {
                viewModelScope.launch(dispatcher) {
                    getCoursesUseCase(
                        categories = listOf(Category.AUDIOCOURSES),
                        forceRefresh = forceRefresh
                    )
                        .onRight { audioCourses ->
                            state = state.copy(
                                isLoading = false,
                                audiocourses = audioCourses,
                            )
                        }
                        .onLeft { throwable: Throwable ->
                            handleThrowable(throwable)
                            state = state.copy(isLoading = false)
                        }
                }
            } catch (e: Exception) {
                handleThrowable(e)
                state = state.copy(isLoading = false)
            }
        }
    }

    private suspend fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is TokenExpiredException -> showTokenExpiredError()
            else -> eventChannel.emit(AudioCoursesListEvent.Error(throwable))
        }
    }

    fun onAction(action: AudioCoursesListAction) {
        when (action) {
            AudioCoursesListAction.OnRefreshContent -> {
                refreshContent()
            }

            else -> Unit
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
            onViewScreen(forceRefresh = true)
            state = state.copy(isRefreshing = false)
        }
    }
}

private const val REFRESH_DELAY = 1500L
