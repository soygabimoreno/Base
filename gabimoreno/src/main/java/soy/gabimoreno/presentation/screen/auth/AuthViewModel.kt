package soy.gabimoreno.presentation.screen.auth

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
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EMAIL
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.exception.TokenExpiredException
import soy.gabimoreno.domain.model.login.Status
import soy.gabimoreno.domain.session.MemberSession
import soy.gabimoreno.domain.usecase.IsBearerTokenValid
import soy.gabimoreno.domain.usecase.LoginUseCase
import soy.gabimoreno.domain.usecase.LoginValidationUseCase
import soy.gabimoreno.domain.usecase.ResetJwtAuthTokenUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase
import soy.gabimoreno.domain.usecase.SetShouldIReloadAudioCoursesUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tracker: Tracker,
    private val loginValidationUseCase: LoginValidationUseCase,
    private val saveCredentialsInDataStoreUseCase: SaveCredentialsInDataStoreUseCase,
    private val memberSession: MemberSession,
    private val loginUseCase: LoginUseCase,
    private val isBearerTokenValid: IsBearerTokenValid,
    private val setShouldIReloadAudioCoursesUseCase: SetShouldIReloadAudioCoursesUseCase,
    private val resetJwtAuthTokenUseCase: ResetJwtAuthTokenUseCase,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    private val eventChannel = MutableSharedFlow<AuthEvent>()
    val events = eventChannel.asSharedFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnViewScreen -> onViewScreen(action.email, action.password)
            is AuthAction.OnEmailChanged -> {
                state = state.copy(email = action.email)
            }

            is AuthAction.OnPasswordChanged -> {
                state = state.copy(password = action.password)
            }

            AuthAction.OnLoginClicked -> onLoginClicked()
            AuthAction.OnLogoutClicked -> onLogoutClicked()
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
                        state = state.copy(
                            isLoading = false,
                            email = email,
                            password = password,
                        )
                        eventChannel.emit(AuthEvent.OnLoginEvent)
                    } else {
                        login(email, password)
                    }
                }

                else -> {
                    showAccess(email, password)
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
            shouldShowAccess = false,
            isLoading = false
        )
        viewModelScope.launch {
            eventChannel.emit(AuthEvent.ShowLoginError)
        }
    }

    private fun showTokenExpiredError() {
        state = state.copy(
            shouldShowAccess = false,
            isLoading = false
        )
        viewModelScope.launch {
            eventChannel.emit(AuthEvent.ShowTokenExpiredError)
        }
    }

    private fun loginSuccessPerform(email: String, password: String) {
        viewModelScope.launch {
            saveCredentialsInDataStoreUseCase(email, password)
            setShouldIReloadAudioCoursesUseCase(true)
            eventChannel.emit(AuthEvent.OnLoginEvent)
            state = state.copy(
                isLoading = false,
                shouldShowAccess = false,
            )
        }
    }

    private fun onLogoutClicked() {
        viewModelScope.launch(dispatcher) {
            memberSession.setActive(false)
            memberSession.setEmail(email = null)
            resetJwtAuthTokenUseCase()
            state = state.copy(
                isLoading = false,
                shouldShowAccess = true,
            )
            setShouldIReloadAudioCoursesUseCase(true)
            eventChannel.emit(AuthEvent.OnLoginEvent)
        }
    }
}
