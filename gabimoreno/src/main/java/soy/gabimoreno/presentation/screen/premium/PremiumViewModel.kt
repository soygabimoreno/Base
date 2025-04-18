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

    var viewState by mutableStateOf<ViewState>(ViewState.Loading)
        private set

    private val _viewEventFlow = MutableSharedFlow<ViewEvent>()
    val viewEventFlow = _viewEventFlow.asSharedFlow()

    fun onViewScreen(
        email: String,
        password: String,
    ) {
        tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
        viewModelScope.launch(dispatcher) {
            val isActive = memberSession.isActive()
            when {
                !isActive -> {
                    ViewEvent.ShowAccess(email, password).emit()
                }

                isActive -> {
                    if (isBearerTokenValid()) {
                        loginSuccessPerform(email, password)
                    } else {
                        login(email, password)
                    }
                }

                else -> {
                    ViewEvent.HideLoading.emit()
                }
            }
        }
    }

    fun onLoginClicked(
        email: String,
        password: String,
    ) {
        val parameters = mapOf(TRACKER_KEY_EMAIL to email)
        tracker.trackEvent(PremiumTrackerEvent.ClickLogin(parameters))
        login(email, password)
    }

    private fun login(
        email: String,
        password: String,
    ) {
        viewState = ViewState.Loading
        loginValidationUseCase(email, password)
            .fold(
                {
                    when (it) {
                        LoginValidationUseCase.Error.InvalidEmailFormat -> {
                            ViewEvent.ShowInvalidEmailFormatError.emit()
                        }

                        LoginValidationUseCase.Error.InvalidPassword -> {
                            ViewEvent.ShowInvalidPasswordError.emit()
                        }
                    }
                }, {
                    ViewEvent.ShowLoading.emit()
                    viewModelScope.launch(dispatcher) {
                        loginUseCase(email, password)
                            .fold(
                                {
                                    viewState = ViewState.Error(it)
                                    ViewEvent.HideLoading.emit()
                                    when (it) {
                                        is TokenExpiredException -> {
                                            ViewEvent.ShowTokenExpiredError(email, password).emit()
                                        }

                                        else -> {
                                            ViewEvent.ShowLoginError(email, password).emit()
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

    private fun loginSuccessPerform(
        email: String,
        password: String,
    ) {
        viewModelScope.launch(dispatcher) {
            viewState = ViewState.Loading
            val categories = Category.entries
            getPremiumAudiosUseCase(categories)
                .fold(
                    {
                        viewState = ViewState.Error(it)
                        ViewEvent.HideLoading.emit()
                        ViewEvent.ShowLoginError(email, password).emit()
                    },
                    { premiumAudios ->
                        viewState = ViewState.Content(premiumAudios)
                        ViewEvent.HideLoading.emit()
                        ViewEvent.ShowPremium(email, password, premiumAudios).emit()
                    }
                )
        }
    }

    fun saveCredentialsInDataStore(
        email: String,
        password: String,
    ) {
        viewModelScope.launch(dispatcher) {
            saveCredentialsInDataStoreUseCase(email, password)
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch(dispatcher) {
            memberSession.setActive(false)
        }
        ViewEvent.ShowAccessAgain.emit()
    }

    private fun ViewEvent.emit() {
        viewModelScope.launch(dispatcher) {
            _viewEventFlow.emit(this@emit)
        }
    }

    fun findPremiumAudioFromId(id: String): PremiumAudio? {
        return when (viewState) {
            is ViewState.Content -> (viewState as ViewState.Content).premiumAudios.find { it.id == id }
            else -> null
        }
    }

    fun refreshContent(
        email: String,
        password: String,
    ) {
        viewModelScope.launch(dispatcher) {
            refreshPremiumAudiosUseCase()
            loginSuccessPerform(email, password)
        }
    }

    sealed class ViewEvent {
        data class ShowAccess(
            val email: String,
            val password: String,
        ) : ViewEvent()

        data object ShowAccessAgain : ViewEvent()
        data object ShowLoading : ViewEvent()
        data object HideLoading : ViewEvent()

        data object ShowInvalidEmailFormatError : ViewEvent()
        data object ShowInvalidPasswordError : ViewEvent()
        data class ShowPremium(
            val email: String,
            val password: String,
            val premiumAudios: List<PremiumAudio>,
        ) : ViewEvent()

        data class ShowLoginError(
            val email: String,
            val password: String,
        ) : ViewEvent()

        data class ShowTokenExpiredError(
            val email: String,
            val password: String,
        ) : ViewEvent()
    }

    sealed class ViewState {
        data object Loading : ViewState() // TODO: Use it instead of the view event
        data class Error(val throwable: Throwable) :
            ViewState() // TODO: Use it instead of the view event

        data class Content(val premiumAudios: List<PremiumAudio>) : ViewState()
    }
}
