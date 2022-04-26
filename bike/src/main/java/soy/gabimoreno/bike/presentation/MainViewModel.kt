package soy.gabimoreno.bike.presentation

import android.bluetooth.BluetoothGatt
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleReadCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.clj.fastble.utils.HexUtil
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

    private val _viewEvents = MutableStateFlow<ViewEvent>(ViewEvent.CheckPermissionsAndInitBle(bleManager))
    val viewEvents: StateFlow<ViewEvent> = _viewEvents.asStateFlow()

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Idle)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    fun onNullBluetoothAdapter() {
        viewModelScope.launch(dispatcher) {
            _viewEvents.value = ViewEvent.Error.ShowDeviceDoesNotSupportBluetooth
        }
    }

    fun onDisabledBluetoothAdapter() {
        viewModelScope.launch(dispatcher) {
            _viewEvents.value = ViewEvent.Error.ShowTurnOnBluetooth
        }
    }

    fun onBleNotSupported() {
        viewModelScope.launch(dispatcher) {
            _viewEvents.value = ViewEvent.Error.ShowDeviceDoesNotSupportBle
        }
    }

    fun startConnection() {
        viewModelScope.launch(dispatcher) {
            bleManager.connect(
                DEVICE_MAC,
                object : BleGattCallback() {
                    override fun onStartConnect() {
                        _viewEvents.value = ViewEvent.Foo
                    }

                    override fun onConnectFail(
                        bleDevice: BleDevice?,
                        exception: BleException?
                    ) {
                        _viewEvents.value = ViewEvent.Error.ShowConnectionFailed
                    }

                    override fun onConnectSuccess(
                        bleDevice: BleDevice?,
                        gatt: BluetoothGatt?,
                        status: Int
                    ) {
                        bleDevice?.let {
                            _viewEvents.value = ViewEvent.ShowDeviceName(bleDevice.name)
                            _viewState.value = ViewState.Content(
                                dataString = "",
                                bleDevice = bleDevice
                            )
                        } ?: run {
                            _viewEvents.value = ViewEvent.Error.ShowNullDeviceConnected
                        }
                    }

                    override fun onDisConnected(
                        isActiveDisConnected: Boolean,
                        device: BleDevice?,
                        gatt: BluetoothGatt?,
                        status: Int
                    ) {
                        _viewEvents.value = ViewEvent.Error.ShowDisconnected
                    }
                })
        }
    }

    fun onReadData() {
        viewModelScope.launch(dispatcher) {
            val bleDevice = (_viewState.value as? ViewState.Content)?.bleDevice
            bleDevice?.let {
                // TODO: This is just for retrieve data. Only should be done once:
//                val services = bleManager.getBluetoothGattServices(bleDevice)
//                val characteristics = bleManager.getBluetoothGattCharacteristics(services[2])
//                val characteristic = characteristics[1]
//                val uuidService = characteristic.service.uuid.toString()
//                val uuidRead = characteristic.uuid.toString()


                bleManager.read(
                    bleDevice,
                    DEVICE_UUID_SERVICE,
                    DEVICE_UUID_READ,
                    object : BleReadCallback() {
                        override fun onReadSuccess(data: ByteArray?) {
                            val dataString = HexUtil.formatHexString(
                                data,
                                true
                            )
                            Log.d(
                                "FOO",
                                "ViewModel, dataString: $dataString"
                            )
                            _viewState.value = ViewState.Content(
                                dataString = dataString,
                                bleDevice = bleDevice
                            )
                        }

                        override fun onReadFailure(exception: BleException?) {
                            _viewEvents.value = ViewEvent.Error.ShowReadFailure
                        }
                    }
                )
            } ?: run {
                _viewEvents.value = ViewEvent.Error.ShowNotLoadedDevice
            }
        }
    }

    sealed class ViewEvent {
        sealed class Error : ViewEvent() {
            object ShowDeviceDoesNotSupportBluetooth : ViewEvent()
            object ShowDeviceDoesNotSupportBle : ViewEvent()
            object ShowTurnOnBluetooth : ViewEvent()
            object ShowConnectionFailed : ViewEvent()
            object ShowNullDeviceConnected : ViewEvent()
            object ShowDisconnected : ViewEvent()
            object ShowReadFailure : ViewEvent()
            object ShowNotLoadedDevice : ViewEvent()
        }

        object Foo : ViewEvent()
        data class CheckPermissionsAndInitBle(val bleManager: BleManager) : ViewEvent()
        data class ShowDeviceName(val deviceName: String) : ViewEvent()
    }

    sealed class ViewState {
        object Idle : ViewState()
        data class Content(
            val dataString: String = "",
            val bleDevice: BleDevice? = null
        ) : ViewState()
    }
}

private const val DEVICE_NAME = "BIKE"
private const val DEVICE_MAC = "C8:C9:A3:E6:77:02"
private const val DEVICE_UUID_SERVICE = "19b10000-e8f2-537e-4f6c-d104768a1214"
private const val DEVICE_UUID_READ = "19b10002-e8f2-537e-4f6c-d104768a1214"
