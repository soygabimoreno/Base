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
import soy.gabimoreno.domain.usecase.LoginUseCase
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
    private val loginUseCase: LoginUseCase,
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
            ViewEvent.ShowAccess(email, password).emit()
        } else {
            ViewEvent.HideLoading.emit()
        }
    }

    fun onLoginClicked(
        email: String,
        password: String
    ) {
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
                                    it.message
                                    ViewEvent.HideLoading.emit()
                                    ViewEvent.ShowLoginError.emit()
                                }, {
                                    ViewEvent.HideLoading.emit()
                                    ViewEvent.ShowPremium(email, password).emit()
                                }
                            )
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
        ViewEvent.ShowAccessAgain.emit()
    }

    private fun ViewEvent.emit() {
        viewModelScope.launch(dispatcher) {
            _viewEventFlow.emit(this@emit)
        }
    }

    sealed class ViewEvent {
        data class ShowAccess(
            val email: String,
            val password: String
        ) : ViewEvent()

        object ShowAccessAgain : ViewEvent()
        object ShowLoading : ViewEvent()
        object HideLoading : ViewEvent()

        object ShowInvalidEmailFormatError : ViewEvent()
        object ShowInvalidPasswordError : ViewEvent()
        data class ShowPremium(
            val email: String,
            val password: String
        ) : ViewEvent()

        object ShowLoginError : ViewEvent()
    }
}
