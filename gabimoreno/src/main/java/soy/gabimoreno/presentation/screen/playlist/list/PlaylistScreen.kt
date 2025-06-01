package soy.gabimoreno.presentation.screen.playlist.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import soy.gabimoreno.R
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.playlist.view.PlaylistDialog
import soy.gabimoreno.presentation.screen.playlist.list.view.PlaylistItem
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White
import soy.gabimoreno.presentation.ui.IconButton

@Composable
fun PlaylistScreenRoot(
    onBackClicked: () -> Unit,
    onItemClick: (playlistId: String) -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel()
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
        }
    )
}

@Composable
fun PlaylistScreen(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.s96),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    padding = Spacing.s16,
                    onClick = { onAction(PlaylistAction.OnBackClicked) },
                )
                Text(
                    text = stringResource(id = R.string.playlists_title).uppercase(),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = Spacing.s16)
                )
            }

            Spacer(modifier = Modifier.padding(vertical = Spacing.s8))
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.shouldIShowAddPlaylistDialog -> {
                    PlaylistDialog(
                        titleDialog = stringResource(id = R.string.playlists_create_title),
                        title = state.dialogTitle,
                        titleError = state.dialogTitleError,
                        description = state.dialogDescription,
                        onTitleChange = { onAction(PlaylistAction.OnDialogTitleChange(it)) },
                        onDescriptionChange = { onAction(PlaylistAction.OnDialogDescriptionChange(it)) },
                        onConfirm = { onAction(PlaylistAction.OnAddPlaylistDialogConfirm) },
                        onDismiss = { onAction(PlaylistAction.OnAddPlaylistDialogDismiss) }
                    )
                }

                state.playlists.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val annotatedLoginString = buildAnnotatedString {
                            append(stringResource(R.string.playlists_empty))
                            append(" ")
                            withStyle(
                                SpanStyle(
                                    color = Orange,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("PLAYLIST")
                            }
                        }
                        Text(
                            text = annotatedLoginString,
                            style = MaterialTheme.typography.h6,
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.s16)
                    ) {
                        items(
                            count = state.playlists.size,
                            key = { state.playlists[it].id }) { index ->
                            PlaylistItem(
                                playlist = state.playlists[index],
                                onItemClick = {
                                    onAction(PlaylistAction.OnItemClicked(state.playlists[index].id))
                                }
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { onAction(PlaylistAction.OnAddNewPlaylistClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Spacing.s16),
            backgroundColor = Orange
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add playlist",
                tint = White
            )
        }

    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun Preview() {
    GabiMorenoTheme {
        PlaylistScreen(
            state = PlaylistState(),
            onAction = {}
        )
    }
}
