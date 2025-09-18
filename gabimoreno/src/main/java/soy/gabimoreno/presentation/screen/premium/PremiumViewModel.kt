package soy.gabimoreno.presentation.screen.premium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import soy.gabimoreno.data.remote.model.getPremiumCategories
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudiosManagedUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase
import soy.gabimoreno.domain.usecase.RefreshBearerTokenUseCase
import soy.gabimoreno.domain.usecase.UpdateAudioItemFavoriteStateUseCase
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel
    @Inject
    constructor(
        private val getPremiumAudiosMediatorUseCase: GetPremiumAudiosManagedUseCase,
        private val getPremiumAudioByIdUseCase: GetPremiumAudioByIdUseCase,
        private val markPremiumAudioAsListenedUseCase: MarkPremiumAudioAsListenedUseCase,
        private val refreshBearerTokenUseCase: RefreshBearerTokenUseCase,
        private val updateAudioItemFavoriteStateUseCase: UpdateAudioItemFavoriteStateUseCase,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        private val _state = MutableStateFlow(PremiumState())
        val state =
            _state
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                    initialValue = PremiumState(),
                )

        private val eventChannel = MutableSharedFlow<PremiumEvent>()
        val events = eventChannel.asSharedFlow()

        fun onViewScreen(shouldIAccessPremium: Boolean) {
            if (shouldIAccessPremium) {
                _state.update { currentState ->
                    currentState.copy(shouldIAccessPremium = true)
                }
                loadPremiumAudios()
            } else {
                _state.update { currentState ->
                    currentState.copy(shouldIAccessPremium = false, isLoading = false)
                }
            }
        }

        fun onAction(action: PremiumAction) {
            when (action) {
                is PremiumAction.OnPremiumItemClicked -> {
                    loadSelectedPremiumAudio(action.premiumAudioId)
                }

                PremiumAction.OnRefreshContent -> refreshContent()
                is PremiumAction.OnListenedToggled -> {
                    viewModelScope.launch(dispatcher) {
                        markPremiumAudioAsListenedUseCase(
                            premiumAudioId = action.premiumAudio.id,
                            hasBeenListened = !action.premiumAudio.hasBeenListened,
                        )
                        val premiumAudioList =
                            _state.value.premiumAudios.map { premiumAudio ->
                                if (premiumAudio.id == action.premiumAudio.id) {
                                    premiumAudio.copy(
                                        hasBeenListened = !action.premiumAudio.hasBeenListened,
                                    )
                                } else {
                                    premiumAudio
                                }
                            }
                        _state.update { currentState ->
                            currentState.copy(premiumAudios = premiumAudioList)
                        }
                    }
                }

                is PremiumAction.OnFavoriteStatusChanged -> {
                    _state.update { currentState ->
                        currentState.copy(
                            premiumAudios =
                                _state.value.premiumAudios.map { premiumAudio ->
                                    if (premiumAudio.id == action.premiumAudio.id) {
                                        premiumAudio.copy(
                                            markedAsFavorite = !premiumAudio.markedAsFavorite,
                                        )
                                    } else {
                                        premiumAudio
                                    }
                                },
                        )
                    }
                    viewModelScope.launch {
                        updateAudioItemFavoriteStateUseCase(
                            action.premiumAudio.id,
                            !action.premiumAudio.markedAsFavorite,
                        )
                    }
                }

                else -> Unit
            }
        }

        fun loadSelectedPremiumAudio(premiumAudioId: String) {
            viewModelScope.launch(dispatcher) {
                val selectedAudio =
                    getPremiumAudioByIdUseCase(premiumAudioId).getOrNull()
                _state.update { currentState ->
                    currentState.copy(
                        selectedPremiumAudio = selectedAudio,
                        premiumAudios =
                            if (selectedAudio == null) {
                                EMPTY_PREMIUM_AUDIOS
                            } else {
                                listOf(selectedAudio)
                            },
                    )
                }

                eventChannel.emit(PremiumEvent.ShowDetail(premiumAudioId))
            }
        }

        private fun loadPremiumAudios() {
            viewModelScope.launch(dispatcher) {
                _state.update { currentState ->
                    currentState.copy(isLoading = true)
                }
                val categories = getPremiumCategories()
                getPremiumAudiosMediatorUseCase(categories)
                    .onLeft { throwable: Throwable ->
                        handleThrowable(throwable)
                        _state.update { currentState ->
                            currentState.copy(isLoading = false)
                        }
                    }.onRight { premiumAudioFlow ->
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                premiumAudioFlow = premiumAudioFlow,
                            )
                        }
                    }
            }
        }

        private suspend fun handleThrowable(throwable: Throwable) {
            when {
                throwable is TokenExpiredException -> refreshToken()
                throwable is HttpException && throwable.code() == TOKEN_EXPIRED_CODE ->
                    refreshToken()

                else -> eventChannel.emit(PremiumEvent.Error(throwable))
            }
        }

        private fun refreshToken() {
            if (state.value.hasRefreshTokenBeenCalled) {
                showLoginError()
            } else {
                _state.update { currentState ->
                    currentState.copy(hasRefreshTokenBeenCalled = true)
                }
                viewModelScope.launch(dispatcher) {
                    refreshBearerTokenUseCase()
                        .onRight {
                            onViewScreen(state.value.shouldIAccessPremium)
                        }.onLeft {
                            showLoginError()
                        }
                }
            }
        }

        private fun showLoginError() {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    shouldIAccessPremium = false,
                )
            }

            viewModelScope.launch {
                eventChannel.emit(PremiumEvent.ShowTokenExpiredError)
            }
        }

        private fun refreshContent() {
            viewModelScope.launch(dispatcher) {
                loadPremiumAudios()
            }
        }
    }

private const val STOP_TIMEOUT_MILLIS = 5_000L
private const val TOKEN_EXPIRED_CODE = 403
