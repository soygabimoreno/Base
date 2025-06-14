package soy.gabimoreno.presentation.screen.playlist.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistCategory
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    playlist: Playlist,
    selectable: Boolean = false,
    isPlaylistSelected: Boolean = false,
    onItemClick: (String) -> Unit = {},
    onToggleClick: (playlistId: Int) -> Unit = {}
) {
    val iconColor by animateColorAsState(
        targetValue = if (isPlaylistSelected) PurpleLight else Black.copy(alpha = 0.2f),
        animationSpec = tween(durationMillis = CHANGE_COLOR_ANIMATION_DURATION),
        label = "selectedPlaylistIconColorAnimation"
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .padding(Spacing.s16)
            .clickable { onItemClick(playlist.id.toString()) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = playlist.category.icon,
            contentDescription = "Playlist",
            modifier = Modifier.size(Spacing.s64),
            tint = playlist.category.color
        )
        Spacer(modifier = Modifier.width(Spacing.s16))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                playlist.title,
                color = Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = Spacing.s4)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.s4)
                    .background(playlist.category.color)
            )
            Text(
                playlist.description,
                color = Black.copy(alpha = 0.8f),
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = Spacing.s4)
            )
        }
        if (selectable) {
            Box(
                modifier = Modifier
                    .size(Spacing.s32)
                    .border(
                        1.dp,
                        playlist.category.color,
                        shape = CircleShape,
                    )
                    .padding(Spacing.s4),
            ) {
                IconButton(
                    onClick = { onToggleClick(playlist.id) },
                ) {
                    Icon(
                        modifier = Modifier
                            .size(Spacing.s32),
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.playlists_add_audio_to_playlist) + " " + playlist.title,
                        tint = iconColor,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PlaylistItemPreview() {
    GabiMorenoTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlaylistItem(
                playlist = Playlist(
                    id = 1,
                    title = "Playlist 1",
                    description = "Description 1",
                    items = emptyList(),
                    position = 0
                )
            )
            PlaylistItem(
                playlist = Playlist(
                    id = 2,
                    title = "Playlist 2",
                    description = "Description 2",
                    items = emptyList(),
                    position = 1
                ),
                selectable = true,
                isPlaylistSelected = true
            )
            PlaylistItem(
                playlist = Playlist(
                    id = 3,
                    title = "Learning CI/CD",
                    description = "Educational roadmap",
                    items = emptyList(),
                    category = PlaylistCategory.ROADMAP_PLAYLIST,
                    position = 2
                ),
                selectable = true,
                isPlaylistSelected = false
            )
        }
    }
}

private const val CHANGE_COLOR_ANIMATION_DURATION = 300
