package soy.gabimoreno.presentation.screen.premium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.PremiumTrackerEvent
import soy.gabimoreno.di.IO
import soy.gabimoreno.remoteconfig.RemoteConfigName
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val tracker: Tracker,
    private val remoteConfigProvider: RemoteConfigProvider,
    @IO private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewEventFlow = MutableSharedFlow<ViewEvent>()
    val viewEventFlow = _viewEventFlow.asSharedFlow()

    fun onViewScreen() {
        tracker.trackEvent(PremiumTrackerEvent.ViewScreen)
        if (remoteConfigProvider.isFeatureEnabled(RemoteConfigName.PREMIUM_SUBSCRIPTION_LAUNCH)) {
            viewModelScope.launch(dispatcher) {
                _viewEventFlow.emit(ViewEvent.ShowLogin)
            }
        }
    }

    sealed class ViewEvent {
        object ShowLogin : ViewEvent()
    }
}
