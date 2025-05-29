package soy.gabimoreno.presentation.screen.premium

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
import retrofit2.HttpException
import soy.gabimoreno.data.remote.model.getPremiumCategories
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudiosManagedUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase
import soy.gabimoreno.domain.usecase.RefreshBearerTokenUseCase
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val getPremiumAudiosMediatorUseCase: GetPremiumAudiosManagedUseCase,
    private val getPremiumAudioByIdUseCase: GetPremiumAudioByIdUseCase,
    private val markPremiumAudioAsListenedUseCase: MarkPremiumAudioAsListenedUseCase,
    private val refreshBearerTokenUseCase: RefreshBearerTokenUseCase,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    var state by mutableStateOf(PremiumState())
        private set

    private val eventChannel = MutableSharedFlow<PremiumEvent>()
    val events = eventChannel.asSharedFlow()

    fun onViewScreen(shouldIAccessPremium: Boolean) {
        if (shouldIAccessPremium) {
            state = state.copy(shouldIAccessPremium = true)
            loadPremiumAudios()
        } else {
            state = state.copy(
                isLoading = false,
                shouldIAccessPremium = false,
            )
        }
    }

    fun onAction(action: PremiumAction) {
        when (action) {
            is PremiumAction.OnPremiumItemClicked -> {
                viewModelScope.launch(dispatcher) {
                    val selectedAudio =
                        getPremiumAudioByIdUseCase(action.premiumAudioId).getOrNull()
                    state =
                        state.copy(
                            selectedPremiumAudio = selectedAudio,
                            premiumAudios = if (selectedAudio == null) EMPTY_PREMIUM_AUDIOS else
                                listOf(selectedAudio)
                        )

                    eventChannel.emit(PremiumEvent.ShowDetail(action.premiumAudioId))
                }
            }

            PremiumAction.OnRefreshContent -> refreshContent()
            is PremiumAction.OnListenedToggled -> {
                viewModelScope.launch(dispatcher) {
                    markPremiumAudioAsListenedUseCase(
                        idPremiumAudio = action.premiumAudio.id,
                        hasBeenListened = !action.premiumAudio.hasBeenListened
                    )
                    val premiumAudioList = state.premiumAudios.map { premiumAudio ->
                        if (premiumAudio.id == action.premiumAudio.id) {
                            premiumAudio.copy(hasBeenListened = !action.premiumAudio.hasBeenListened)
                        } else {
                            premiumAudio
                        }
                    }
                    state = state.copy(premiumAudios = premiumAudioList)
                }
            }

            else -> Unit
        }
    }

    private fun loadPremiumAudios() {
        viewModelScope.launch(dispatcher) {
            state = state.copy(isLoading = true)
            val categories = getPremiumCategories()
            getPremiumAudiosMediatorUseCase(categories)
                .onLeft { throwable: Throwable ->
                    handleThrowable(throwable)
                    state = state.copy(isLoading = false)
                }
                .onRight { premiumAudioFlow ->
                    state = state.copy(
                        isLoading = false,
                        premiumAudioFlow = premiumAudioFlow,
                    )
                }
        }
    }

    private suspend fun handleThrowable(throwable: Throwable) {
        when {
            throwable is TokenExpiredException -> refreshToken()
            throwable is HttpException && throwable.code() == TOKEN_EXPIRED_CODE -> refreshToken()
            else -> eventChannel.emit(PremiumEvent.Error(throwable))
        }
    }


    private fun refreshToken() {
        if (state.hasRefreshTokenBeenCalled) {
            showLoginError()
        } else {
            state = state.copy(hasRefreshTokenBeenCalled = true)
            viewModelScope.launch(dispatcher) {
                refreshBearerTokenUseCase()
                    .onRight {
                        onViewScreen(state.shouldIAccessPremium)
                    }
                    .onLeft {
                        showLoginError()
                    }
            }
        }
    }

    private fun showLoginError() {
        state = state.copy(
            isLoading = false,
            shouldIAccessPremium = false,
        )
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

private const val TOKEN_EXPIRED_CODE = 403
