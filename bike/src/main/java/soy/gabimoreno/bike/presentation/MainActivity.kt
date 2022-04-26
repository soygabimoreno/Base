package soy.gabimoreno.bike.presentation

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.clj.fastble.BleManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import soy.gabimoreno.bike.R
import soy.gabimoreno.bike.ui.theme.BaseTheme
import soy.gabimoreno.bike.ui.views.MainScreen
import soy.gabimoreno.framework.toast

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LaunchedEffect(true) { collectViewEvents() }
                    MainScreen(viewModel)
                }
            }
        }
    }

    private suspend fun collectViewEvents() {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        when {
            bluetoothAdapter == null -> viewModel.onNullBluetoothAdapter()
            !bluetoothAdapter.isEnabled -> viewModel.onDisabledBluetoothAdapter()
        }

        viewModel.viewEvents.collect { viewEvent ->
            when (viewEvent) {
                MainViewModel.ViewEvent.Foo -> foo()
                is MainViewModel.ViewEvent.InitBle -> initBle(viewEvent.bleManager)
                MainViewModel.ViewEvent.ShowDeviceDoesNotSupportBluetooth -> showDeviceDoesNotSupportBluetooth()
                MainViewModel.ViewEvent.ShowDeviceDoesNotSupportBle -> showDeviceDoesNotSupportBle()
                MainViewModel.ViewEvent.ShowTurnOnBluetooth -> showTurnOnBluetooth()
            }
        }
    }

    private fun foo() {
        // Do nothing
    }

    private fun initBle(bleManager: BleManager) {
        bleManager.init(application)

        if (!bleManager.isSupportBle) {
            viewModel.onBleNotSupported()
            return
        }

        bleManager.enableLog(true)
            .setReConnectCount(
                RECONNECT_COUNT,
                RECONNECT_INTERVAL_IN_MILLIS
            )
            .setSplitWriteNum(SPLIT_WRITE_NUM)
            .setConnectOverTime(CONNECT_OVER_TIME_IN_MILLIS)
            .operateTimeout = OPERATE_TIME_OUT

        // TODO: Request permissions on runtime for BLUETOOTH_SCAN
        // mainViewModel.requestPermissions()

        viewModel.startScan()
    }

    private fun showDeviceDoesNotSupportBluetooth() {
        toast(R.string.device_does_not_support_bluetooth)
    }

    private fun showDeviceDoesNotSupportBle() {
        toast(R.string.device_does_not_support_ble)
    }

    private fun showTurnOnBluetooth() {
        toast(R.string.turn_on_bluetooth)
    }

    private fun startScan() {
        toast(R.string.scanning)
    }
}

private const val RECONNECT_COUNT = 1
private const val RECONNECT_INTERVAL_IN_MILLIS = 5_000L
private const val SPLIT_WRITE_NUM = 20
private const val CONNECT_OVER_TIME_IN_MILLIS = 10_000L
private const val OPERATE_TIME_OUT = 5_000
