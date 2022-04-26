package soy.gabimoreno.bike.ui.views

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
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            fontSize = 128.sp,
            text = (viewState as? MainViewModel.ViewState.Content)?.dataString ?: ""
        )
    }
}
