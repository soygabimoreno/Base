package soy.gabimoreno.presentation.screen.courses.detail.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Percent
import soy.gabimoreno.presentation.theme.PinkBright
import soy.gabimoreno.presentation.theme.PurpleDark
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun ItemAudioCourse(
    audioCourseItem: AudioCourseItem,
    audioCourseTitle: String,
    onItemClicked: (audioCourseItem: AudioCourseItem) -> Unit,
    onItemListenedToggled: (audioCourseItem: AudioCourseItem) -> Unit,
    onAddToPlaylistClicked: (audioCourseItemId: String) -> Unit,
    onFavoriteStatusChanged: (audioCourseItem: AudioCourseItem) -> Unit,
) {
    val iconColor by animateColorAsState(
        targetValue =
            if (audioCourseItem.hasBeenListened) {
                PurpleDark
            } else {
                Black.copy(alpha = Percent.TEN)
            },
        animationSpec = tween(durationMillis = CHANGE_COLOR_ANIMATION_DURATION),
        label = "checkIconColorAnimation",
    )
    val iconFavoriteColor by animateColorAsState(
        targetValue =
            if (audioCourseItem.markedAsFavorite) {
                PinkBright
            } else {
                Black.copy(
                    alpha = Percent.TWENTY,
                )
            },
        animationSpec = tween(durationMillis = CHANGE_COLOR_ANIMATION_DURATION),
        label = "favoriteIconColorAnimation",
    )
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(Spacing.s8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier =
                Modifier
                    .clickable { onItemClicked(audioCourseItem) }
                    .weight(Percent.EIGHTY),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(audioCourseItem.title, color = Black, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(Spacing.s4))
            Text(
                audioCourseTitle,
                color = Black.copy(alpha = Percent.SEVENTY),
                fontWeight = FontWeight.Light,
            )
        }
        IconButton(
            modifier = Modifier,
            onClick = { onItemListenedToggled(audioCourseItem) },
        ) {
            Icon(
                modifier =
                    Modifier
                        .weight(Percent.TEN)
                        .size(Spacing.s32),
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.course_listened),
                tint = iconColor,
            )
        }
        Spacer(modifier = Modifier.width(Spacing.s8))
        IconButton(
            modifier = Modifier.weight(Percent.TEN),
            onClick = { onFavoriteStatusChanged(audioCourseItem) },
        ) {
            Icon(
                modifier =
                    Modifier
                        .size(Spacing.s32),
                imageVector =
                    if (audioCourseItem.markedAsFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                contentDescription = stringResource(R.string.audio_favorite),
                tint = iconFavoriteColor,
            )
        }
        Spacer(modifier = Modifier.width(Spacing.s8))
        IconButton(
            modifier = Modifier.weight(Percent.TEN),
            onClick = { onAddToPlaylistClicked(audioCourseItem.id) },
        ) {
            Icon(
                modifier =
                    Modifier
                        .size(Spacing.s32),
                imageVector = Icons.Default.LibraryMusic,
                contentDescription = stringResource(R.string.playlists_add_audio_to_playlist),
                tint = PurpleLight,
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun ItemAudioCoursePreview() {
    GabiMorenoTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(White)
                    .border(Spacing.s1, Gray),
            verticalArrangement = Arrangement.Center,
        ) {
            ItemAudioCourse(
                audioCourseTitle = "Audio curso",
                audioCourseItem =
                    AudioCourseItem(
                        id = "1",
                        title = "item title",
                        url = "item url",
                        hasBeenListened = true,
                    ),
                onItemClicked = {},
                onItemListenedToggled = {},
                onAddToPlaylistClicked = {},
                onFavoriteStatusChanged = {},
            )
            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Black.copy(alpha = 0.2f)),
            )
            ItemAudioCourse(
                audioCourseTitle = "Audio curso",
                audioCourseItem =
                    AudioCourseItem(
                        id = "1",
                        title = "item title",
                        url = "item url",
                        hasBeenListened = false,
                        markedAsFavorite = true,
                    ),
                onItemClicked = {},
                onItemListenedToggled = {},
                onAddToPlaylistClicked = {},
                onFavoriteStatusChanged = {},
            )
        }
    }
}

private const val CHANGE_COLOR_ANIMATION_DURATION = 300
