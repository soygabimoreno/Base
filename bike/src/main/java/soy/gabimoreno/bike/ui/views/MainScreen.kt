package soy.gabimoreno.bike.ui.views

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import soy.gabimoreno.bike.presentation.MainViewModel

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    if (bluetoothAdapter == null) {
        // Device doesn't support Bluetooth
    }

    val mainViewModel: MainViewModel = hiltViewModel()
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

@Preview(
    widthDp = 540,
    heightDp = 960
)
@Composable
fun Preview() {
    MainScreen()
}
