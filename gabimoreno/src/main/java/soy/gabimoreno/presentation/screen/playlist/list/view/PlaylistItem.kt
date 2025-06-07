package soy.gabimoreno.presentation.screen.playlist.list.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    playlist: Playlist,
    onItemClick: (String) -> Unit = {}
) {
    val isOdd = playlist.position % 2 == 1
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
            imageVector = Icons.Default.LibraryMusic,
            contentDescription = "Playlist",
            modifier = Modifier.size(Spacing.s64),
            tint = if (isOdd) Orange else PurpleLight
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
                    .background(if (isOdd) Orange else PurpleLight)
            )
            Text(
                playlist.description,
                color = Black.copy(alpha = 0.8f),
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = Spacing.s4)
            )
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
                )
            )
            PlaylistItem(
                playlist = Playlist(
                    id = 3,
                    title = "Playlist 3",
                    description = "Description 3",
                    items = emptyList(),
                    position = 2
                )
            )
        }
    }
}
