package soy.gabimoreno.bike.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
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
    private val bleManager: BleManager,
    @IO private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _viewEvents = MutableStateFlow<ViewEvent>(ViewEvent.InitBle(bleManager))
    val viewEvents: StateFlow<ViewEvent> = _viewEvents.asStateFlow()

    fun onNullBluetoothAdapter() {
        viewModelScope.launch(dispatcher) {
            _viewEvents.value = ViewEvent.ShowDeviceDoesNotSupportBluetooth
        }
    }

    fun onDisabledBluetoothAdapter() {
        viewModelScope.launch(dispatcher) {
            _viewEvents.value = ViewEvent.ShowTurnOnBluetooth
        }
    }

    fun onBleNotSupported() {
        viewModelScope.launch(dispatcher) {
            _viewEvents.value = ViewEvent.ShowDeviceDoesNotSupportBle
        }
    }

    fun startScan() {
        viewModelScope.launch(dispatcher) {
            bleManager.scan(
                object : BleScanCallback() {
                    override fun onScanStarted(success: Boolean) {
                        _viewEvents.value = ViewEvent.Foo
                    }

                    override fun onScanning(bleDevice: BleDevice?) {
                        _viewEvents.value = ViewEvent.Foo
                    }

                    override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                        _viewEvents.value = ViewEvent.Foo
                    }
                }
            )
        }
    }

    sealed class ViewEvent {
        object Foo : ViewEvent()
        data class InitBle(val bleManager: BleManager) : ViewEvent()
        object ShowDeviceDoesNotSupportBluetooth : ViewEvent()
        object ShowDeviceDoesNotSupportBle : ViewEvent()
        object ShowTurnOnBluetooth : ViewEvent()
    }
}
