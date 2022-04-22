package soy.gabimoreno.bike.presentation

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import soy.gabimoreno.bike.R
import soy.gabimoreno.bike.ui.theme.BaseTheme
import soy.gabimoreno.bike.ui.views.MainScreen
import soy.gabimoreno.framework.toast

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val mainViewModel: MainViewModel = hiltViewModel()
                    LaunchedEffect(true) {
                        collect(mainViewModel)
                    }
                    MainScreen(mainViewModel)
                }
            }
        }
    }

    private suspend fun collect(mainViewModel: MainViewModel) {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            mainViewModel.onNullBluetoothAdapter()
        } else {
            mainViewModel.startScan()
        }

        mainViewModel.viewEvents.collect { viewEvent ->
            when (viewEvent) {
                MainViewModel.ViewEvent.ShowInitialState -> showInitialState()
                MainViewModel.ViewEvent.ShowDeviceDoesNotSupportBluetooth -> showDeviceDoesNotSupportBluetooth()
                MainViewModel.ViewEvent.StartScan -> startScan()
            }
        }
    }

    private fun showInitialState() {
        // Do nothing
    }

    private fun showDeviceDoesNotSupportBluetooth() {
        toast(R.string.device_does_not_support_bluetooth)
    }

    private fun startScan() {
        toast(R.string.scanning)

    }
}
