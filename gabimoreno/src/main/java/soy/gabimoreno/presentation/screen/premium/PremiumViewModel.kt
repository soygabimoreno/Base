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
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EMAIL
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.model.login.Status
import soy.gabimoreno.domain.session.MemberSession
import soy.gabimoreno.domain.usecase.GetPremiumAudiosUseCase
import soy.gabimoreno.domain.usecase.IsBearerTokenValid
import soy.gabimoreno.domain.usecase.LoginUseCase
import soy.gabimoreno.domain.usecase.LoginValidationUseCase
import soy.gabimoreno.domain.usecase.RefreshPremiumAudiosUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val tracker: Tracker,
    private val loginValidationUseCase: LoginValidationUseCase,
    private val saveCredentialsInDataStoreUseCase: SaveCredentialsInDataStoreUseCase,
    private val memberSession: MemberSession,
    private val loginUseCase: LoginUseCase,
    private val getPremiumAudiosUseCase: GetPremiumAudiosUseCase,
    private val isBearerTokenValid: IsBearerTokenValid,
    private val refreshPremiumAudiosUseCase: RefreshPremiumAudiosUseCase,
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
                println(action.email)
                state = state.copy(email = action.email)
            }

            is PremiumAction.OnPasswordChanged -> {
                println(action.password)
                state = state.copy(password = action.password)
            }

            is PremiumAction.OnPremiumItemClicked -> {
                viewModelScope.launch {
                    eventChannel.emit(PremiumEvent.ShowDetail(action.premiumAudioId))
                }
            }

            PremiumAction.OnLoginClicked -> onLoginClicked()
            PremiumAction.OnLogoutClicked -> onLogoutClicked()
            PremiumAction.OnRefreshContent -> refreshContent()
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
            val categories = Category.entries
            getPremiumAudiosUseCase(categories)
                .fold(
                    {
                        eventChannel.emit(PremiumEvent.Error(it))
                        showLoginError()
                    },
                    { premiumAudios ->
                        state = state.copy(
                            showInvalidEmailFormatError = false,
                            showInvalidPasswordError = false,
                            shouldShowPremium = true,
                            shouldShowAccess = false,
                            isLoading = false,
                            premiumAudios = premiumAudios
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
        }
    }

    fun findPremiumAudioFromId(id: String): PremiumAudio? {
        return state.premiumAudios.find { it.id == id }
    }

    private fun refreshContent() {
        viewModelScope.launch(dispatcher) {
            refreshPremiumAudiosUseCase()
            loginSuccessPerform(state.email, state.password)
        }
    }
}
