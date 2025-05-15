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
import soy.gabimoreno.data.remote.model.getPremiumCategories
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EMAIL
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.model.login.Status
import soy.gabimoreno.domain.session.MemberSession
import soy.gabimoreno.domain.usecase.GetPremiumAudioByIdUseCase
import soy.gabimoreno.domain.usecase.GetPremiumAudiosManagedUseCase
import soy.gabimoreno.domain.usecase.IsBearerTokenValid
import soy.gabimoreno.domain.usecase.LoginUseCase
import soy.gabimoreno.domain.usecase.LoginValidationUseCase
import soy.gabimoreno.domain.usecase.MarkPremiumAudioAsListenedUseCase
import soy.gabimoreno.domain.usecase.ResetJwtAuthTokenUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val tracker: Tracker,
    private val loginValidationUseCase: LoginValidationUseCase,
    private val saveCredentialsInDataStoreUseCase: SaveCredentialsInDataStoreUseCase,
    private val memberSession: MemberSession,
    private val loginUseCase: LoginUseCase,
    private val getPremiumAudiosMediatorUseCase: GetPremiumAudiosManagedUseCase,
    private val getPremiumAudioByIdUseCase: GetPremiumAudioByIdUseCase,
    private val isBearerTokenValid: IsBearerTokenValid,
    private val markPremiumAudioAsListenedUseCase: MarkPremiumAudioAsListenedUseCase,
    private val setShouldIReloadAudioCoursesUseCase: SetShouldIReloadAudioCoursesUseCase,
    private val resetJwtAuthTokenUseCase: ResetJwtAuthTokenUseCase,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    var state by mutableStateOf(PremiumState())
        private set

    private val eventChannel = MutableSharedFlow<PremiumEvent>()
    val events = eventChannel.asSharedFlow()

    fun onAction(action: PremiumAction) {
        when (action) {
            is PremiumAction.OnViewScreen -> onViewScreen(action.email, action.password)
            is PremiumAction.OnEmailChanged -> {
                state = state.copy(email = action.email)
            }

            is PremiumAction.OnPasswordChanged -> {
                state = state.copy(password = action.password)
            }

            is PremiumAction.OnPremiumItemClicked -> {
                viewModelScope.launch {
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

            PremiumAction.OnLoginClicked -> onLoginClicked()
            PremiumAction.OnLogoutClicked -> onLogoutClicked()
            PremiumAction.OnRefreshContent -> refreshContent()
            is PremiumAction.OnListenedToggled -> {
                viewModelScope.launch {
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
        }
    }

    fun onViewScreen(
        email: String,
        password: String,
    ) {
        tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
        viewModelScope.launch(dispatcher) {
            val isActive = memberSession.isActive()
            when {
                !isActive -> {
                    showAccess(email, password)
                }

                isActive -> {
                    if (isBearerTokenValid()) {
                        loginSuccessPerform(email, password)
                    } else {
                        login(email, password)
                    }
                }

                else -> {
                    state = state.copy(
                        isLoading = false,
                        shouldShowAccess = true
                    )
                }
            }
        }
    }

    private fun showAccess(email: String, password: String) {
        state = state.copy(
            isLoading = false,
            shouldShowAccess = true,
            email = email,
            password = password
        )
    }

    private fun onLoginClicked() {
        val parameters = mapOf(TRACKER_KEY_EMAIL to state.email)
        tracker.trackEvent(PremiumTrackerEvent.ClickLogin(parameters))
        login(state.email, state.password)
    }

    private fun login(email: String, password: String) {
        state = state.copy(isLoading = true)
        loginValidationUseCase(email, password)
            .fold(
                {
                    when (it) {
                        LoginValidationUseCase.Error.InvalidEmailFormat -> {
                            state =
                                state.copy(
                                    showInvalidEmailFormatError = true,
                                    showInvalidPasswordError = false,
                                    isLoading = false
                                )
                        }

                        LoginValidationUseCase.Error.InvalidPassword -> {
                            state = state.copy(
                                showInvalidPasswordError = true,
                                showInvalidEmailFormatError = false,
                                isLoading = false
                            )
                        }
                    }
                }, {
                    viewModelScope.launch(dispatcher) {
                        loginUseCase(email, password)
                            .fold(
                                {
                                    when (it) {
                                        is TokenExpiredException -> {
                                            showTokenExpiredError()
                                        }

                                        else -> {
                                            showLoginError()
                                        }
                                    }
                                }, { member ->
                                    val isActive = member.isActive || member.status == Status.FREE
                                    memberSession.setActive(isActive)
                                    loginSuccessPerform(email, password)
                                    setShouldIReloadAudioCoursesUseCase(true)
                                }
                            )
                    }
                }
            )
    }

    private fun showLoginError() {
        state = state.copy(
            shouldShowAccess = true,
            shouldShowPremium = false,
            isLoading = false
        )
        viewModelScope.launch {
            eventChannel.emit(PremiumEvent.ShowLoginError)
        }
    }

    private fun showTokenExpiredError() {
        state = state.copy(
            shouldShowAccess = true,
            shouldShowPremium = false,
            isLoading = false
        )
        viewModelScope.launch {
            eventChannel.emit(PremiumEvent.ShowTokenExpiredError)
        }
    }

    private fun loginSuccessPerform(email: String, password: String) {
        viewModelScope.launch(dispatcher) {
            state = state.copy(isLoading = true)
            val categories = getPremiumCategories()
            getPremiumAudiosMediatorUseCase(categories).fold(
                {
                    eventChannel.emit(PremiumEvent.Error(it))
                    showLoginError()
                }, { premiumAudioFlow ->
                    state = state.copy(
                        showInvalidEmailFormatError = false,
                        showInvalidPasswordError = false,
                        shouldShowAccess = false,
                        shouldShowPremium = true,
                        isLoading = false,
                        premiumAudioFlow = premiumAudioFlow
                    )
                    saveCredentialsInDataStore(email, password)
                }
            )
        }
    }

    private fun saveCredentialsInDataStore(
        email: String,
        password: String,
    ) {
        viewModelScope.launch(dispatcher) {
            saveCredentialsInDataStoreUseCase(email, password)
        }
    }

    private fun onLogoutClicked() {
        viewModelScope.launch(dispatcher) {
            memberSession.setActive(false)
            state = state.copy(
                shouldShowAccess = true,
                shouldShowPremium = false
            )
            resetJwtAuthTokenUseCase()
            setShouldIReloadAudioCoursesUseCase(true)
        }
    }

    private fun refreshContent() {
        viewModelScope.launch(dispatcher) {
            loginSuccessPerform(state.email, state.password)
        }
    }
}
