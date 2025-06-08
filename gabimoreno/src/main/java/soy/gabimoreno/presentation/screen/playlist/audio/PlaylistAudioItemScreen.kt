package soy.gabimoreno.presentation.screen.playlist.audio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun PlaylistAudioItemRoot(
    playlistAudioId: String,
    onBackClicked: () -> Unit,
    viewModel: PlaylistAudioItemViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.onViewScreen(playlistAudioId)
        println("AddAudiosToPlaylistScreenRoot $playlistAudioId")
    }

    PlaylistAudioItem(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                PlaylistAudioItemAction.OnBackClicked -> onBackClicked()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun PlaylistAudioItem(
    state: PlaylistAudioItemState,
    onAction: (PlaylistAudioItemAction) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Back to previous screen",
            modifier = Modifier.clickable { onAction(PlaylistAudioItemAction.OnBackClicked) })
        Spacer(modifier = Modifier.padding(vertical = Spacing.s32))
        Text(state.selectedAudioTitle)
    }

}

@Preview
@Composable
private fun Preview() {
    GabiMorenoTheme {
        PlaylistAudioItem(
            state = PlaylistAudioItemState(),
            onAction = {}
        )
    }
}
