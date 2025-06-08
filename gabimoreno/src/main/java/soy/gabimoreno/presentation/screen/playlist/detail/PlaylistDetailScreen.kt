package soy.gabimoreno.presentation.screen.playlist.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import soy.gabimoreno.presentation.screen.courses.list.AudioCoursesListAction
import soy.gabimoreno.presentation.screen.playlist.list.PlaylistAction
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun PlaylistDetailScreenRoot(
    playlistId: String,
    onBackClicked: () -> Unit,
    viewModel: PlaylistDetailViewModel = hiltViewModel()
) {
    println("TEST $playlistId")

    PlaylistDetailScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is PlaylistDetailAction.OnBackClicked -> onBackClicked()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun PlaylistDetailScreen(
    state: PlaylistDetailState,
    onAction: (PlaylistDetailAction) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            "Back to previous screen",
            modifier = Modifier.clickable { onAction(PlaylistDetailAction.OnBackClicked) })
        Spacer(modifier = Modifier.padding(vertical = Spacing.s32))
    }
}

@Preview
@Composable
private fun Preview() {
    GabiMorenoTheme {
        PlaylistDetailScreen(
            state = PlaylistDetailState(),
            onAction = {}
        )
    }
}
