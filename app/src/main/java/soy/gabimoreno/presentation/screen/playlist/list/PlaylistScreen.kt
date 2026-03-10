package soy.gabimoreno.presentation.screen.playlist.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import soy.gabimoreno.R
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.playlist.list.view.ReorderablePlaylistColumn
import soy.gabimoreno.presentation.screen.playlist.view.PlaylistDialog
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White
import soy.gabimoreno.presentation.ui.IconButton
import soy.gabimoreno.presentation.ui.dialog.CustomDialog
import soy.gabimoreno.presentation.ui.dialog.TypeDialog

@Composable
fun PlaylistScreenRoot(
    onBackClicked: () -> Unit,
    onItemClick: (playlistId: String) -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PlaylistEvent.Error -> {
                    context.toast(context.getString(R.string.unexpected_error))
                }
            }
        }
    }

    PlaylistScreen(
        state = state,
        onAction = { action ->
            when (action) {
                PlaylistAction.OnBackClicked -> onBackClicked()
                is PlaylistAction.OnItemClicked -> onItemClick(action.playlistId.toString())
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
fun PlaylistScreen(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .fillMaxSize(),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PlaylistToolbar { onAction(PlaylistAction.OnBackClicked) }

            Spacer(modifier = Modifier.padding(vertical = Spacing.s8))

            PlaylistScreenContent(state, onAction)
        }

        FloatingActionButton(
            onClick = { onAction(PlaylistAction.OnAddNewPlaylistClicked) },
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Spacing.s16)
                    .padding(bottom = Spacing.s64),
            backgroundColor = Orange,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add playlist",
                tint = White,
            )
        }
    }
}

@Composable
private fun PlaylistToolbar(onBackClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back),
            padding = Spacing.s16,
            onClick = onBackClick,
        )
        Text(
            text = stringResource(id = R.string.playlists_title).uppercase(),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(start = Spacing.s16),
        )
    }
}

@Composable
private fun PlaylistScreenContent(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
) {
    when {
        state.isLoading -> PlaylistLoading()

        state.shouldIShowAddPlaylistDialog -> PlaylistAddDialog(state, onAction)

        state.playlists.isEmpty() -> PlaylistEmptyContent()

        state.shouldIShowConfirmDialog -> PlaylistConfirmDialog(onAction)

        else -> PlaylistContentList(state, onAction)
    }
}

@Composable
private fun PlaylistAddDialog(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
) {
    PlaylistDialog(
        titleDialog = stringResource(id = R.string.playlists_create_title),
        title = state.dialogTitle,
        titleError = state.dialogTitleError,
        description = state.dialogDescription,
        onTitleChange = { onAction(PlaylistAction.OnDialogTitleChange(it)) },
        onDescriptionChange = { onAction(PlaylistAction.OnDialogDescriptionChange(it)) },
        onConfirm = { onAction(PlaylistAction.OnAddPlaylistConfirmDialog) },
        onDismiss = { onAction(PlaylistAction.OnAddPlaylistDismissDialog) },
    )
}

@Composable
private fun PlaylistEmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val annotatedLoginString =
            buildAnnotatedString {
                append(stringResource(R.string.playlists_empty))
                append(" ")
                withStyle(
                    SpanStyle(
                        color = Orange,
                        fontWeight = FontWeight.Bold,
                    ),
                ) {
                    append(stringResource(R.string.playlists_name).uppercase())
                }
            }

        Text(
            text = annotatedLoginString,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Composable
private fun PlaylistConfirmDialog(onAction: (PlaylistAction) -> Unit) {
    CustomDialog(
        title = stringResource(R.string.profile_reset_dialog_title),
        text = stringResource(R.string.playlist_delete_playlist_confirmation),
        confirmText = stringResource(R.string.playlists_remove),
        dismissText = stringResource(R.string.close),
        onConfirm = { onAction(PlaylistAction.OnConfirmDeleteDialog) },
        onDismiss = { onAction(PlaylistAction.OnDismissDeleteDialog) },
        typeDialog = TypeDialog.CONFIRMATION,
    )
}

@Composable
private fun PlaylistContentList(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
) {
    ReorderablePlaylistColumn(
        playlists = state.playlists,
        onItemClick = { playlistId ->
            onAction(PlaylistAction.OnItemClicked(playlistId))
        },
        onDragFinish = { playlists ->
            onAction(PlaylistAction.OnItemDragFinish(playlists))
        },
        onRemovePlaylistClicked = { playlistId ->
            onAction(PlaylistAction.OnRemovePlaylistClicked(playlistId))
        },
    )
}

@Composable
private fun PlaylistLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun Preview() {
    GabiMorenoTheme {
        PlaylistScreen(
            state = PlaylistState(),
            onAction = {},
        )
    }
}
