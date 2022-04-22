package soy.gabimoreno.bike.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import soy.gabimoreno.bike.di.IO
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _viewEvents = MutableStateFlow<ViewEvent>(ViewEvent.ShowInitialState)
    val viewEvents: StateFlow<ViewEvent> = _viewEvents.asStateFlow()

    fun onNullBluetoothAdapter() {
        viewModelScope.launch(dispatcher) {
            _viewEvents.value = ViewEvent.ShowDeviceDoesNotSupportBluetooth
        }
    }

    fun startScan() {
        viewModelScope.launch(dispatcher) {
            _viewEvents.value = ViewEvent.StartScan
        }
    }

    sealed class ViewEvent {
        object ShowInitialState : ViewEvent()
        object ShowDeviceDoesNotSupportBluetooth : ViewEvent()
        object StartScan : ViewEvent()
    }
}
