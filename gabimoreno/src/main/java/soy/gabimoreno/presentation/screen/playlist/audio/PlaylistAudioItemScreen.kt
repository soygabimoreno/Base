package soy.gabimoreno.presentation.screen.playlist.audio

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import soy.gabimoreno.R
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.playlist.view.PlaylistItem
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.IconButton
import soy.gabimoreno.presentation.ui.button.PrimaryButton
import soy.gabimoreno.presentation.ui.button.SecondaryButton

@Composable
fun PlaylistAudioItemRoot(
    playlistAudioId: String,
    onBackClicked: () -> Unit,
    onNewPlaylistClicked: () -> Unit,
    viewModel: PlaylistAudioItemViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.onViewScreen(playlistAudioId)
        viewModel.events.collect { event ->
            when (event) {
                is PlaylistAudioItemEvent.Error -> {
                    context.toast(context.getString(R.string.unexpected_error))
                }

                is PlaylistAudioItemEvent.Success -> {
                    context.toast(context.getString(R.string.playlists_success_message))
                }
            }
        }
    }

    PlaylistAudioItem(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                PlaylistAudioItemAction.OnBackClicked -> onBackClicked()
                PlaylistAudioItemAction.OnNewPlaylistClicked -> onNewPlaylistClicked()
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
                    .height(Spacing.s64),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    padding = Spacing.s16,
                    onClick = { onAction(PlaylistAudioItemAction.OnBackClicked) },
                )
                Text(
                    text = stringResource(id = R.string.playlists_add_audio_to_playlist).uppercase(),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = Spacing.s16),
                )
            }
            Text(
                state.selectedAudioTitle,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(horizontal = Spacing.s8),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(vertical = Spacing.s8))
            SecondaryButton(
                text = stringResource(R.string.playlists_create_title),
                height = Spacing.s48,
                onClick = { onAction(PlaylistAudioItemAction.OnNewPlaylistClicked) },
            )
            Spacer(modifier = Modifier.padding(vertical = Spacing.s8))
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }

                    state.playlists.isEmpty() -> {
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
                                append(stringResource(R.string.playlists_name))
                            }
                        }
                        Text(
                            text = annotatedLoginString,
                            style = MaterialTheme.typography.h6,
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.s16)
                        ) {
                            items(
                                count = state.playlists.size,
                                key = { state.playlists[it].playlist.id }) { index ->
                                PlaylistItem(
                                    playlist = state.playlists[index].playlist,
                                    selectable = true,
                                    isPlaylistSelected = state.playlists[index].isSelected,
                                    onToggleClick = {
                                        onAction(
                                            PlaylistAudioItemAction.OnTogglePlaylist(
                                                state.playlists[index].playlist.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                PrimaryButton(
                    text = stringResource(R.string.playlists_save),
                    height = Spacing.s48,
                    modifier = Modifier
                        .padding(bottom = Spacing.s64)
                        .align(Alignment.BottomCenter),
                    onClick = { onAction(PlaylistAudioItemAction.OnSaveClicked) },
                )
            }
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
        PlaylistAudioItem(
            state = PlaylistAudioItemState(
                selectedAudioTitle = "CI/CD starting with GitHub Actions"
            ),
            onAction = {}
        )
    }
}
