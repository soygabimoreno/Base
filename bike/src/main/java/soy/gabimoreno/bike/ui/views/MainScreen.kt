package soy.gabimoreno.bike.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import soy.gabimoreno.bike.presentation.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    val dataString = (viewState as? MainViewModel.ViewState.Content)?.dataString ?: ""
    Log.d(
        "FOO",
        "MainScreen, dataString: $dataString"
    )
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            fontSize = 128.sp,
            text = "21"
        )
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
//        Text(
//            text = "Bike",
//            modifier = Modifier.padding(16.dp)
//        )
    }
}
