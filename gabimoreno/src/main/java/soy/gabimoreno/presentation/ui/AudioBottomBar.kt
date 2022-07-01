package soy.gabimoreno.presentation.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.navigationBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.Podcast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import kotlin.math.roundToInt

@Composable
fun AudioBottomBar(
    modifier: Modifier = Modifier
) {
    val episode = ViewModelProvider.playerViewModel.currentPlayingEpisode.value

    AnimatedVisibility(
        visible = episode != null,
        modifier = modifier
    ) {
        if (episode != null) {
            AudioBottomBarContent(episode)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AudioBottomBarContent(episode: Episode) {
    val swipeableState = rememberSwipeableState(0)
    val podcastPlayer = ViewModelProvider.playerViewModel

    val endAnchor = LocalConfiguration.current.screenWidthDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )

    val iconResId =
        if (podcastPlayer.podcastIsPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.54f) },
                orientation = Orientation.Horizontal
            )
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                podcastPlayer.stopPlayback()
            }
        }

        AudioBottomBarStatelessContent(
            episode = episode,
            xOffset = swipeableState.offset.value.roundToInt(),
            icon = iconResId,
            onTooglePlaybackState = {
                podcastPlayer.togglePlaybackState()
            }
        ) {
            podcastPlayer.showPlayerFullScreen = true
        }
    }
}

@Composable
fun AudioBottomBarStatelessContent(
    episode: Episode,
    xOffset: Int,
    @DrawableRes icon: Int,
    onTooglePlaybackState: () -> Unit,
    onTap: (Offset) -> Unit,
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(xOffset, 0) }
            .background(Color(0xFF343434))
            .navigationBarsPadding()
            .height(64.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = onTap
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberCoilPainter(episode.thumbnailUrl),
                contentDescription = stringResource(R.string.podcast_thumbnail),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp),
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp),
            ) {
                Text(
                    episode.title,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    episode.podcast.title,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.graphicsLayer {
                        alpha = 0.60f
                    }
                )
            }

            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(R.string.play),
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onTooglePlaybackState)
                    .padding(6.dp)
            )
        }
    }
}

@Preview(name = "Bottom Bar")
@Composable
fun AudioBottomBarPreview() {
    PreviewContent {
        AudioBottomBarStatelessContent(
            episode = Episode(
                id = "1",
                url = "",
                audioUrl = "",
                imageUrl = "https://picsum.photos/200",
                podcast = Podcast("This is publisher", "This is podcast title"),
                thumbnailUrl = "https://picsum.photos/200",
                pubDateMillis = 0,
                title = "This is a title",
                audioLengthSeconds = 2700,
                description = "This is a description"
            ),
            xOffset = 0,
            icon = R.drawable.ic_baseline_play_arrow_24,
            onTooglePlaybackState = { },
            onTap = { }
        )
    }
}
