package soy.gabimoreno.presentation.screen.courses.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.AudioCoursesListTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.usecase.GetAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.GetShouldIReloadAudioCoursesUseCase
import soy.gabimoreno.domain.usecase.RefreshBearerTokenUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import javax.inject.Inject

@HiltViewModel
class AudioCoursesListViewModel
    @Inject
    constructor(
        private val getCoursesUseCase: GetAudioCoursesUseCase,
        private val getShouldIReloadAudioCoursesUseCase: GetShouldIReloadAudioCoursesUseCase,
        private val setShouldIReloadAudioCoursesUseCase: SetShouldIReloadAudioCoursesUseCase,
        private val refreshBearerTokenUseCase: RefreshBearerTokenUseCase,
        private val tracker: Tracker,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        private var hasLoadedInitialData = false

        private val _state = MutableStateFlow(AudioCoursesListState())
        val state =
            _state
                .onStart {
                    if (!hasLoadedInitialData) {
                        hasLoadedInitialData = true
                        onScreenView()
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                    initialValue = AudioCoursesListState(),
                )

        private val eventChannel = MutableSharedFlow<AudioCoursesListEvent>()
        val events = eventChannel.asSharedFlow()

        private fun onScreenView() {
            tracker.trackEvent(AudioCoursesListTrackerEvent.ViewScreen)
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
            _state.update { currentState ->
                currentState.copy(isLoading = true)
            }
            viewModelScope.launch(dispatcher) {
                try {
                    getCoursesUseCase(
                        categories = listOf(Category.AUDIOCOURSES),
                        forceRefresh = forceRefresh,
                    ).onRight { audioCourses ->
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                audiocourses = audioCourses,
                            )
                        }
                    }.onLeft { throwable: Throwable ->
                        handleThrowable(throwable)
                        _state.update { currentState ->
                            currentState.copy(isLoading = false)
                        }
                    }
                } catch (e: Exception) {
                    handleThrowable(e)
                    _state.update { currentState ->
                        currentState.copy(isLoading = false)
                    }
                }
            }
        }

        private suspend fun handleThrowable(throwable: Throwable) {
            when {
                throwable is TokenExpiredException -> refreshToken()
                throwable is HttpException && throwable.code() == TOKEN_EXPIRED_CODE ->
                    refreshToken()

                else -> eventChannel.emit(AudioCoursesListEvent.Error(throwable))
            }
        }

        private fun refreshToken() {
            if (state.value.hasRefreshTokenBeenCalled) {
                showTokenExpiredError()
            } else {
                _state.update { currentState ->
                    currentState.copy(hasRefreshTokenBeenCalled = true)
                }
                viewModelScope.launch(dispatcher) {
                    refreshBearerTokenUseCase()
                        .onRight {
                            onViewScreen(forceRefresh = true)
                        }.onLeft {
                            showTokenExpiredError()
                        }
                }
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
                _state.update { currentState ->
                    currentState.copy(isRefreshing = true)
                }
                delay(REFRESH_DELAY)
                onViewScreen(forceRefresh = true)
                _state.update { currentState ->
                    currentState.copy(isRefreshing = false)
                }
            }
        }
    }

private const val REFRESH_DELAY = 1_500L
private const val STOP_TIMEOUT_MILLIS = 5_000L
private const val TOKEN_EXPIRED_CODE = 403
