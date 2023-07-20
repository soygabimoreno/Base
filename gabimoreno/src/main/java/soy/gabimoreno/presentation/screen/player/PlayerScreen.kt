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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults.IndicatorBackgroundOpacity
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.systemBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.data.tracker.domain.toPlayPause
import soy.gabimoreno.domain.model.audio.Audio
import soy.gabimoreno.domain.model.audio.Saga
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.framework.calculatePaletteColor
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.ArrowDownButton
import soy.gabimoreno.presentation.ui.EmphasisText
import soy.gabimoreno.presentation.ui.PreviewContent
import kotlin.math.roundToInt

@Composable
fun PlayerScreen(backDispatcher: OnBackPressedDispatcher) {
    val playerViewModel = ViewModelProvider.playerViewModel
    val audio = playerViewModel.currentPlayingAudio.value

    val showPlayerFullScreen = playerViewModel.showPlayerFullScreen
    if (showPlayerFullScreen) {
        audio?.let {
            playerViewModel.onViewScreen(audio)
        }
    }

    AnimatedVisibility(
        visible = audio != null && showPlayerFullScreen,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        )
    ) {
        if (audio != null) {
            PodcastPlayerBody(audio, backDispatcher)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PodcastPlayerBody(
    audio: Audio,
    backDispatcher: OnBackPressedDispatcher,
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
        .data(audio.imageUrl)
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

    val sliderProgress =
        if (sliderIsChanging) localSliderValue else playerViewModel.currentAudioProgress

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
            audio = audio,
            imagePainter = imagePainter,
            gradientColor = gradientColor,
            yOffset = swipeableState.offset.value.roundToInt(),
            playPauseIcon = iconResId,
            playbackProgress = sliderProgress,
            currentTime = playerViewModel.getCurrentPlaybackFormattedPosition(),
            totalTime = playerViewModel.currentAudioFormattedDuration,
            onRewind = {
                playerViewModel.onRewindClicked()
            },
            onForward = {
                playerViewModel.onForwardClicked()
            },
            onSkipToPrevious = {
                playerViewModel.onSkipToPrevious()
            },
            onSkipToNext = {
                playerViewModel.onSkipToNext()
            },
            onTooglePlayback = {
                playerViewModel.onPlayPauseClickedFromPlayer(audio, isPlaying.toPlayPause())
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
    audio: Audio,
    imagePainter: Painter,
    gradientColor: Color,
    yOffset: Int,
    @DrawableRes playPauseIcon: Int,
    playbackProgress: Float,
    currentTime: String,
    totalTime: String,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onSkipToPrevious: () -> Unit,
    onSkipToNext: () -> Unit,
    onTooglePlayback: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onSliderChangeFinished: () -> Unit,
    onClose: () -> Unit,
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
                        modifier = Modifier.padding(horizontal = Spacing.s24)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = Spacing.s32)
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
                            audio.title,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onBackground,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Text(
                            audio.saga.title,
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
                                .padding(vertical = Spacing.s24)
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
                                .padding(top = Spacing.s8, bottom = Spacing.s32),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_skip_previous_24),
                                contentDescription = stringResource(R.string.skip_to_previous),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onSkipToPrevious)
                                    .padding(Spacing.oddSpacing12)
                                    .size(Spacing.s32)
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_replay_10_24),
                                contentDescription = stringResource(R.string.replay_10_seconds),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onRewind)
                                    .padding(Spacing.oddSpacing12)
                                    .size(Spacing.s32)
                            )
                            Icon(
                                painter = painterResource(playPauseIcon),
                                contentDescription = stringResource(R.string.play),
                                tint = MaterialTheme.colors.background,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colors.onBackground)
                                    .clickable(onClick = onTooglePlayback)
                                    .size(Spacing.s64)
                                    .padding(Spacing.s8)
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_forward_10_24),
                                contentDescription = stringResource(R.string.forward_10_seconds),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onForward)
                                    .padding(Spacing.oddSpacing12)
                                    .size(Spacing.s32)
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_skip_next_24),
                                contentDescription = stringResource(R.string.skip_to_next),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = onSkipToNext)
                                    .padding(Spacing.oddSpacing12)
                                    .size(Spacing.s32)
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
            audio = Episode(
                id = "1",
                url = "",
                audioUrl = "",
                imageUrl = "",
                saga = Saga("This is publisher", "This is podcast title"),
                thumbnailUrl = "",
                pubDateMillis = 0,
                title = "This is a title",
                audioLengthInSeconds = 2700,
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
            onSkipToPrevious = {},
            onSkipToNext = {},
            onRewind = { },
            onTooglePlayback = { },
            onSliderChange = { },
            onSliderChangeFinished = { }
        )
    }
}
