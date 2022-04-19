package soy.gabimoreno.bike.ui.views

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import soy.gabimoreno.bike.R
import soy.gabimoreno.bike.presentation.MainViewModel
import soy.gabimoreno.framework.toast

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val viewEvents by mainViewModel.viewEvents.collectAsState()
    when (viewEvents) {
        MainViewModel.ViewEvent.InitialState -> {
            context.toast(stringResource(id = R.string.initialized))
        }
        MainViewModel.ViewEvent.ShowDeviceDoesNotSupportBluetooth -> {
            context.toast(stringResource(id = R.string.device_does_not_support_bluetooth))
        }
    }

    val bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    if (bluetoothAdapter == null) {
        mainViewModel.onNullBluetoothAdapter()
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            fontSize = 64.sp,
            text = "23"
        )
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = "Bike",
            modifier = Modifier.padding(16.dp)
        )
    }
}
