package soy.gabimoreno.presentation.screen.premium

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
import soy.gabimoreno.domain.usecase.LoginValidationUseCase
import soy.gabimoreno.domain.usecase.SaveCredentialsInDataStoreUseCase
import soy.gabimoreno.remoteconfig.RemoteConfigName
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val tracker: Tracker,
    private val remoteConfigProvider: RemoteConfigProvider,
    private val loginValidationUseCase: LoginValidationUseCase,
    private val saveCredentialsInDataStoreUseCase: SaveCredentialsInDataStoreUseCase,
    @IO private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewEventFlow = MutableSharedFlow<ViewEvent>()
    val viewEventFlow = _viewEventFlow.asSharedFlow()

    fun onViewScreen(
        email: String,
        password: String
    ) {
        tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
        if (remoteConfigProvider.isFeatureEnabled(RemoteConfigName.PREMIUM_SUBSCRIPTION_LAUNCH)) {
            viewModelScope.launch(dispatcher) {
                _viewEventFlow.emit(ViewEvent.ShowAccess(email, password))
            }
        } else {
            viewModelScope.launch(dispatcher) {
                _viewEventFlow.emit(ViewEvent.HideLoading)
            }
        }
    }

    fun onLoginClicked(
        email: String,
        password: String
    ) {
        loginValidationUseCase(email, password)
            .fold(
                {
                    viewModelScope.launch(dispatcher) {
                        when (it) {
                            LoginValidationUseCase.Error.InvalidEmailFormat -> _viewEventFlow.emit(
                                ViewEvent.ShowInvalidEmailFormatError
                            )
                            LoginValidationUseCase.Error.InvalidPassword -> _viewEventFlow.emit(
                                ViewEvent.ShowInvalidPasswordError
                            )
                        }
                    }
                }, {
                    viewModelScope.launch(dispatcher) {
                        _viewEventFlow.emit(ViewEvent.ShowPremium(email, password))
                    }
                }
            )


        val parameters = mapOf(
            TRACKER_KEY_EMAIL to email
        )
        tracker.trackEvent(PremiumTrackerEvent.ClickLogin(parameters))
    }

    fun saveCredentialsInDataStore(
        email: String,
        password: String
    ) {
        viewModelScope.launch(dispatcher) {
            saveCredentialsInDataStoreUseCase(email, password)
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch(dispatcher) {
            _viewEventFlow.emit(ViewEvent.ShowAccessAgain)
        }
    }

    sealed class ViewEvent {
        data class ShowAccess(
            val email: String,
            val password: String
        ) : ViewEvent()

        object ShowAccessAgain : ViewEvent()
        object HideLoading : ViewEvent()

        object ShowInvalidEmailFormatError : ViewEvent()
        object ShowInvalidPasswordError : ViewEvent()
        data class ShowPremium(
            val email: String,
            val password: String
        ) : ViewEvent()
    }
}
