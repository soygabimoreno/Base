package soy.gabimoreno.presentation.screen.player

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ProgressIndicatorDefaults.IndicatorBackgroundOpacity
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.systemBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.data.tracker.domain.toPlayPause
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.Podcast
import soy.gabimoreno.framework.calculatePaletteColor
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.ui.ArrowDownButton
import soy.gabimoreno.presentation.ui.EmphasisText
import soy.gabimoreno.presentation.ui.PreviewContent
import kotlin.math.roundToInt

@Composable
fun PlayerScreen(backDispatcher: OnBackPressedDispatcher) {
    val playerViewModel = ViewModelProvider.playerViewModel
    val episode = playerViewModel.currentPlayingEpisode.value

    val showPlayerFullScreen = playerViewModel.showPlayerFullScreen
    if (showPlayerFullScreen) {
        episode?.let {
            playerViewModel.onViewScreen(episode)
        }
    }

    AnimatedVisibility(
        visible = episode != null && showPlayerFullScreen,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        )
    ) {
        if (episode != null) {
            PodcastPlayerBody(episode, backDispatcher)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PodcastPlayerBody(
    episode: Episode,
    backDispatcher: OnBackPressedDispatcher
) {
    val playerViewModel = ViewModelProvider.playerViewModel
    val swipeableState = rememberSwipeableState(0)
    val endAnchor = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                playerViewModel.showPlayerFullScreen = false
            }
        }
    }

    val backgroundColor = MaterialTheme.colors.background
    var gradientColor by remember {
        mutableStateOf(backgroundColor)
    }

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(episode.imageUrl)
        .target {
            calculatePaletteColor(it) { color ->
                gradientColor = color
            }
        }
        .build()

    val imagePainter = rememberCoilPainter(request = imageRequest)

    val isPlaying = playerViewModel.podcastIsPlaying
    val iconResId =
        if (isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24

    var sliderIsChanging by remember { mutableStateOf(false) }

    var localSliderValue by remember { mutableStateOf(0f) }

    val sliderProgress = if (sliderIsChanging) localSliderValue else playerViewModel.currentEpisodeProgress

    Box(
        modifier = Modifier
            .fillMaxSize()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.34f) },
                orientation = Orientation.Vertical
            )
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                playerViewModel.showPlayerFullScreen = false
            }
        }

        PodcastPlayerStatelessContent(
            episode = episode,
            imagePainter = imagePainter,
            gradientColor = gradientColor,
            yOffset = swipeableState.offset.value.roundToInt(),
            playPauseIcon = iconResId,
            playbackProgress = sliderProgress,
            currentTime = playerViewModel.currentPlaybackFormattedPosition,
            totalTime = playerViewModel.currentEpisodeFormattedDuration,
            onRewind = {
                playerViewModel.onRewindClicked()
            },
            onForward = {
                playerViewModel.onForwardClicked()
            },
            onTooglePlayback = {
                playerViewModel.onPlayPauseClickedFromPlayer(episode, isPlaying.toPlayPause())
                playerViewModel.togglePlaybackState()
            },
            onSliderChange = { newPosition ->
                localSliderValue = newPosition
                sliderIsChanging = true
            },
            onSliderChangeFinished = {
                playerViewModel.onSliderChangeFinished(localSliderValue)
                sliderIsChanging = false
            }
        ) {
            playerViewModel.showPlayerFullScreen = false
        }
    }

    LaunchedEffect("playbackPosition") {
        playerViewModel.updateCurrentPlaybackPosition()
    }

    DisposableEffect(backDispatcher) {
        backDispatcher.addCallback(backCallback)

        onDispose {
            backCallback.remove()
            playerViewModel.showPlayerFullScreen = false
        }
    }
}

@Composable
fun PodcastPlayerStatelessContent(
    episode: Episode,
    imagePainter: Painter,
    gradientColor: Color,
    yOffset: Int,
    @DrawableRes playPauseIcon: Int,
    playbackProgress: Float,
    currentTime: String,
    totalTime: String,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onTooglePlayback: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onSliderChangeFinished: () -> Unit,
    onClose: () -> Unit
) {
    val gradientColors = listOf(gradientColor, MaterialTheme.colors.background)

    val sliderColors = SliderDefaults.colors(
        thumbColor = MaterialTheme.colors.onBackground,
        activeTrackColor = MaterialTheme.colors.onBackground,
        inactiveTrackColor = MaterialTheme.colors.onBackground.copy(
            alpha = IndicatorBackgroundOpacity
        ),
    )


    Box(
        modifier = Modifier
            .offset { IntOffset(0, yOffset) }
            .fillMaxSize()
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = gradientColors,
                            endY = LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density / 2
                        )
                    )
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                Column {
                    ArrowDownButton(onClick = onClose)
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 32.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .weight(1f, fill = false)
                                .align(Alignment.CenterHorizontally)
                                .aspectRatio(1f)
                                .background(MaterialTheme.colors.onBackground.copy(alpha = 0.08f))
                        ) {
                            Image(
                                painter = imagePainter,
                                contentDescription = stringResource(R.string.podcast_thumbnail),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }

                        Text(
                            episode.title,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onBackground,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Text(
                            episode.podcast.title,
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.graphicsLayer {
                                alpha = 0.60f
                            }
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        ) {
                            Slider(
                                value = playbackProgress,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = sliderColors,
                                onValueChange = onSliderChange,
                                onValueChangeFinished = onSliderChangeFinished,
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                EmphasisText(text = currentTime)
                                EmphasisText(text = totalTime)
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 32.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_replay_10_24),
                                contentDescription = stringResource(R.string.replay_10_seconds),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onRewind)
                                    .padding(12.dp)
                                    .size(32.dp)
                            )
                            Icon(
                                painter = painterResource(playPauseIcon),
                                contentDescription = stringResource(R.string.play),
                                tint = MaterialTheme.colors.background,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colors.onBackground)
                                    .clickable(onClick = onTooglePlayback)
                                    .size(64.dp)
                                    .padding(8.dp)
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_forward_10_24),
                                contentDescription = stringResource(R.string.forward_10_seconds),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onForward)
                                    .padding(12.dp)
                                    .size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Player")
@Composable
fun PodcastPlayerPreview() {
    PreviewContent {
        PodcastPlayerStatelessContent(
            episode = Episode(
                id = "1",
                url = "",
                audioUrl = "",
                imageUrl = "",
                podcast = Podcast("This is publisher", "This is podcast title"),
                thumbnailUrl = "",
                pubDateMillis = 0,
                title = "This is a title",
                audioLengthSeconds = 2700,
                description = "This is a description"
            ),
            imagePainter = painterResource(id = R.drawable.ic_baseline_mic_24),
            gradientColor = Color.DarkGray,
            yOffset = 0,
            playPauseIcon = R.drawable.ic_baseline_play_arrow_24,
            playbackProgress = 0f,
            currentTime = "0:00",
            totalTime = "10:00",
            onClose = { },
            onForward = { },
            onRewind = { },
            onTooglePlayback = { },
            onSliderChange = { },
            onSliderChangeFinished = { }
        )
    }
}
