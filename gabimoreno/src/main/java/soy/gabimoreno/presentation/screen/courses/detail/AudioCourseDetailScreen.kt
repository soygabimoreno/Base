package soy.gabimoreno.presentation.screen.courses.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import soy.gabimoreno.R
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.audio.Saga
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.framework.toast
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.courses.detail.view.ItemAudioCourse
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Pink
import soy.gabimoreno.presentation.theme.PurpleDark
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White
import soy.gabimoreno.presentation.ui.BackButton

@Composable
fun AudioCoursesDetailScreenRoot(
    audioCourseId: String,
    onBackClicked: () -> Unit,
) {
    val context = LocalContext.current
    val coursesDetailViewModel = ViewModelProvider.audioCourseDetailViewModel
    val playerViewModel = ViewModelProvider.playerViewModel
    LaunchedEffect(Unit) {
        coursesDetailViewModel.onViewScreen(audioCourseId)
        coursesDetailViewModel.events.collect { event ->
            when (event) {
                is AudioCourseDetailEvent.Error -> {
                    context.toast(context.getString(R.string.unexpected_error))
                }

                AudioCourseDetailEvent.PlayAudio -> {
                    if (coursesDetailViewModel.state.audio != null) {
                        playerViewModel.playPauseAudio(
                            coursesDetailViewModel.state.audios,
                            coursesDetailViewModel.state.audio as Episode
                        )
                    }
                }
            }
        }
    }
    AudioCourseDetailScreen(
        state = coursesDetailViewModel.state,
        onAction = { action ->
            when (action) {
                is AudioCourseDetailAction.OnBackClicked -> onBackClicked()
                else -> Unit
            }
            coursesDetailViewModel.onAction(action)
        }
    )
}

@Composable
fun AudioCourseDetailScreen(
    state: AudioCourseDetailState,
    onAction: (AudioCourseDetailAction) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        if (state.audioCourse != null && !state.isLoading) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.TopStart
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.audioCourse.thumbnailUrl)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = stringResource(R.string.course_thumbnail),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    error = painterResource(R.drawable.ic_audiocourses_header),
                    placeholder = painterResource(R.drawable.ic_audiocourses_header),
                )
                Box(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                        .clip(CircleShape)
                        .background(PurpleDark.copy(alpha = 0.8f))
                ) {
                    BackButton(
                        modifier = Modifier.size(Spacing.s64),
                        tint = Orange,
                        onClick = { onAction(AudioCourseDetailAction.OnBackClicked) }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(Pink)
                    .padding(horizontal = Spacing.s16, vertical = Spacing.s8)
            ) {
                Text(
                    state.audioCourse.title,
                    color = White,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    state.audioCourse.excerpt,
                    color = White,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W300
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(horizontal = Spacing.s8)
                    .border(1.dp, Black.copy(alpha = 0.2f)),
                verticalArrangement = Arrangement.spacedBy(Spacing.s16),
            ) {
                items(state.audioCourse.audios.size) { index ->
                    ItemAudioCourse(
                        audioCourseItem = state.audioCourse.audios[index],
                        audioCourseTitle = state.audioCourse.title,
                        onItemClicked = {
                            onAction(
                                AudioCourseDetailAction.OnAudioCourseItemClicked(
                                    state.audioCourse.audios[index]
                                )
                            )
                        },
                        onItemListenedToggled = {
                            onAction(
                                AudioCourseDetailAction.OnAudioItemListenedToggled(
                                    state.audioCourse.audios[index]
                                )
                            )
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Black.copy(alpha = 0.2f))
                    )
                }
                item {
                    if (state.audioCourse.audios.size < FREE_AUDIO_SIZE) {
                        Spacer(modifier = Modifier.padding(bottom = Spacing.s32))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .background(Orange),
                            onClick = {
                                uriHandler.openUri(state.audioCourse.url)
                            }
                        ) {
                            Text(
                                stringResource(R.string.course_link),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(bottom = Spacing.s96))
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AudioCourseDetailPreview() {
    GabiMorenoTheme {
        AudioCourseDetailScreen(
            state = AudioCourseDetailState(
                audioCourse = AudioCourse(
                    id = "1-1",
                    url = "",
                    videoUrl = "",
                    thumbnailUrl = "",
                    pubDateMillis = 0,
                    title = "Audiocurso de CI/CD",
                    audioLengthInSeconds = 2700,
                    excerpt = "Domina con este curso CI/CD para AHORRAR TIEMPO Y EVITAR ERRORES en tu dia a  dia como programador",
                    category = Category.AUDIOCOURSES,
                    description = "excerpt",
                    saga = Saga(author = "This is publisher", title = "This is saga title"),
                    isPurchased = false,
                    audios = emptyList()
                ),
                isLoading = false
            ),
            onAction = {}
        )
    }
}

private const val FREE_AUDIO_SIZE = 8
