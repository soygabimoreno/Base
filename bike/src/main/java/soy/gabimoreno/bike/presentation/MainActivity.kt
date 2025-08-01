package soy.gabimoreno.bike.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.os.Bundle
import android.os.CountDownTimer
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
import soy.gabimoreno.bike.R
import soy.gabimoreno.bike.framework.PermissionRequester
import soy.gabimoreno.bike.framework.openAppSettings
import soy.gabimoreno.bike.ui.theme.BaseTheme
import soy.gabimoreno.bike.ui.views.MainScreen
import soy.gabimoreno.framework.toast

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val bluetoothScanPermission =
        PermissionRequester(
            activity = this,
            permission = Manifest.permission.BLUETOOTH_SCAN,
            onShowRationale = {
                toast(R.string.permission_bluetooth_scan_rationale)
            },
            onDenied = {
                toast(R.string.permission_bluetooth_scan_denied)
                openAppSettings()
            },
        )

    private val bluetoothConnectPermission =
        PermissionRequester(
            activity = this,
            permission = Manifest.permission.BLUETOOTH_CONNECT,
            onShowRationale = {
                toast(R.string.permission_bluetooth_connect_rationale)
            },
            onDenied = {
                toast(R.string.permission_bluetooth_connect_denied)
                openAppSettings()
            },
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
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
                MainViewModel.ViewEvent.Error.ShowDeviceDoesNotSupportBluetooth ->
                    toast(
                        R.string.device_does_not_support_bluetooth,
                    )

                MainViewModel.ViewEvent.Error.ShowDeviceDoesNotSupportBle ->
                    toast(
                        R.string.device_does_not_support_ble,
                    )

                MainViewModel.ViewEvent.Error.ShowTurnOnBluetooth ->
                    toast(
                        R.string.turn_on_bluetooth,
                    )
                MainViewModel.ViewEvent.Error.ShowConnectionFailed ->
                    toast(
                        R.string.connection_failed,
                    )
                MainViewModel.ViewEvent.Error.ShowNullDeviceConnected ->
                    toast(
                        R.string.problem_with_connected_device,
                    )
                MainViewModel.ViewEvent.Error.ShowDisconnected -> toast(R.string.disconnected)
                MainViewModel.ViewEvent.Error.ShowReadFailure -> toast(R.string.read_failure)
                MainViewModel.ViewEvent.Error.ShowNotLoadedDevice ->
                    toast(
                        R.string.not_loaded_device,
                    )
                MainViewModel.ViewEvent.Foo -> Unit
                is MainViewModel.ViewEvent.CheckPermissionsAndInitBle ->
                    checkPermissionsAndInitBle(
                        viewEvent.bleManager,
                    )

                is MainViewModel.ViewEvent.ShowDeviceName -> showDeviceName(viewEvent.deviceName)
            }
        }
    }

    private fun checkPermissionsAndInitBle(bleManager: BleManager) {
        bluetoothScanPermission.runWithPermission {
            bluetoothConnectPermission.runWithPermission {
                initBle(bleManager)
            }
        }
    }

    private fun initBle(bleManager: BleManager) {
        bleManager.init(application)

        if (!bleManager.isSupportBle) {
            viewModel.onBleNotSupported()
            return
        }

        bleManager
            .enableLog(true)
            .setReConnectCount(
                RECONNECT_COUNT,
                RECONNECT_INTERVAL_IN_MILLIS,
            ).setSplitWriteNum(SPLIT_WRITE_NUM)
            .setConnectOverTime(CONNECT_OVER_TIME_IN_MILLIS)
            .operateTimeout = OPERATE_TIME_OUT
        viewModel.startConnection()
    }

    private fun showDeviceName(deviceName: String) {
        toast("Connected to $deviceName")
        // TODO: Change CountDownTimer by a Timer
        object : CountDownTimer(
            COUNTDOWN_DURATION_MINUTES * SECONDS_PER_MINUTE * ONE_SECOND_IN_MILLIS,
            COUNTDOWN_INTERVAL_IN_MILLIS,
        ) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.onReadData()
            }

            override fun onFinish() {
                // Do nothing
            }
        }.start()
    }
}

private const val CONNECT_OVER_TIME_IN_MILLIS = 10_000L
private const val COUNTDOWN_DURATION_MINUTES = 60
private const val COUNTDOWN_INTERVAL_IN_MILLIS = 200L
private const val ONE_SECOND_IN_MILLIS = 1_000L
private const val OPERATE_TIME_OUT = 5_000
private const val RECONNECT_COUNT = 1
private const val RECONNECT_INTERVAL_IN_MILLIS = 5_000L
private const val SECONDS_PER_MINUTE = 60
private const val SPLIT_WRITE_NUM = 20
