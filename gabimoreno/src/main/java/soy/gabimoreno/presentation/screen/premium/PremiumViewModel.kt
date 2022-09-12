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
import soy.gabimoreno.data.network.mapper.toPremiumAudios
import soy.gabimoreno.data.network.model.Category
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EMAIL
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.content.Post
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.domain.usecase.*
import soy.gabimoreno.framework.datastore.DataStoreMemberSession
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val tracker: Tracker,
    private val loginValidationUseCase: LoginValidationUseCase,
    private val saveCredentialsInDataStoreUseCase: SaveCredentialsInDataStoreUseCase,
    private val dataStoreMemberSession: DataStoreMemberSession,
    private val loginUseCase: LoginUseCase,
    private val getPremiumPostsUseCase: GetPremiumPostsUseCase,
    private val isBearerTokenValid: IsBearerTokenValid,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    var viewState by mutableStateOf<ViewState>(ViewState.Loading)
        private set

    private val _viewEventFlow = MutableSharedFlow<PremiumViewModel.ViewEvent>()
    val viewEventFlow = _viewEventFlow.asSharedFlow()

    fun onViewScreen(
        email: String,
        password: String,
    ) {
        tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
        viewModelScope.launch(dispatcher) {
            val isActive = dataStoreMemberSession.isActive()
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
                                    ViewEvent.ShowLoginError(email, password).emit()
                                }, { member ->
                                    val isUserActive = member.isActive
                                    dataStoreMemberSession.setActive(isUserActive)
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
            val categories = Category.values().toList()
            getPremiumPostsUseCase(categories)
                .fold(
                    {
                        viewState = ViewState.Error(it)
                        ViewEvent.HideLoading.emit()
                        ViewEvent.ShowLoginError(email, password).emit()
                    },
                    { posts ->
                        viewState = ViewState.Content(posts.toPremiumAudios())
                        ViewEvent.HideLoading.emit()
                        ViewEvent.ShowPremium(email, password, posts).emit()
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
            dataStoreMemberSession.setActive(false)
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

    sealed class ViewEvent {
        data class ShowAccess(
            val email: String,
            val password: String,
        ) : ViewEvent()

        object ShowAccessAgain : ViewEvent()
        object ShowLoading : ViewEvent()
        object HideLoading : ViewEvent()

        object ShowInvalidEmailFormatError : ViewEvent()
        object ShowInvalidPasswordError : ViewEvent()
        data class ShowPremium(
            val email: String,
            val password: String,
            val posts: List<Post>,
        ) : ViewEvent()

        data class ShowLoginError(
            val email: String,
            val password: String,
        ) : ViewEvent()
    }

    sealed class ViewState {
        object Loading : ViewState() // TODO: Use it instead of the view event
        data class Error(val throwable: Throwable) :
            ViewState() // TODO: Use it instead of the view event

        data class Content(val premiumAudios: List<PremiumAudio>) : ViewState()
    }
}
