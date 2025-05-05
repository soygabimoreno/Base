package soy.gabimoreno.presentation.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.data.remote.mapper.EMPTY_AUDIO_LENGTH_IN_SECONDS
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.tracker.domain.toPlayPause
import soy.gabimoreno.domain.model.audio.Audio
import soy.gabimoreno.domain.model.content.PremiumAudio
import soy.gabimoreno.framework.parseFromHtmlFormat
import soy.gabimoreno.presentation.navigation.Feature
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.HomeViewModel
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.AudioImage
import soy.gabimoreno.presentation.ui.BackButton
import soy.gabimoreno.presentation.ui.EmphasisText
import soy.gabimoreno.presentation.ui.SelectableEmphasisText
import soy.gabimoreno.presentation.ui.button.PrimaryButton
import soy.gabimoreno.util.formatMillisecondsAsDate
import soy.gabimoreno.util.toDurationMinutes

@Composable
fun DetailScreen(
    audioId: String,
    feature: Feature,
    onBackClicked: () -> Unit,
) {
    val currentContext = LocalContext.current
    val scrollState = rememberScrollState()
    val homeViewModel = ViewModelProvider.homeViewModel
    val detailViewModel = ViewModelProvider.detailViewModel
    val playerViewModel = ViewModelProvider.playerViewModel
    val premiumViewModel = ViewModelProvider.premiumViewModel

    var audios: List<Audio>? = null
    var audio: Audio? = null
    when (feature) {
        Feature.PODCAST -> {
            audios =
                (homeViewModel.viewState as HomeViewModel.ViewState.Success).episodes
            audio = homeViewModel.findEpisodeFromId(audioId)
        }

        Feature.PREMIUM -> {
            val viewState = premiumViewModel.state
            audios = viewState.premiumAudios
            audio = premiumViewModel.state.selectedPremiumAudio
        }

        else -> Unit

    }

    LaunchedEffect(Unit) {
        audio?.let {
            detailViewModel.onViewScreen(audio)
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            Row {
                BackButton {
                    audio?.let {
                        detailViewModel.onBackClicked(audio)
                    }
                    onBackClicked()
                }
            }

            if (audio != null) {
                val isPlaying = playerViewModel.podcastIsPlaying &&
                    playerViewModel.currentPlayingAudio.value?.id == audio.id
                val playButtonText =
                    if (isPlaying) stringResource(R.string.pause) else stringResource(R.string.play)

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .navigationBarsPadding()
                        .padding(vertical = Spacing.s24, horizontal = Spacing.s16)
                        .padding(bottom = if (playerViewModel.currentPlayingAudio.value != null) Spacing.s64 else Spacing.s0)

                ) {
                    AudioImage(
                        url = audio.imageUrl,
                        modifier = Modifier.height(Spacing.oddSpacing120)
                    )

                    Spacer(modifier = Modifier.height(Spacing.s32))

                    Text(
                        audio.title,
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(Spacing.s24))

                    Text(
                        audio.saga.author,
                        style = MaterialTheme.typography.body1
                    )

                    val dateAndDurationText =
                        if (audio.audioLengthInSeconds != EMPTY_AUDIO_LENGTH_IN_SECONDS) {
                            "${audio.pubDateMillis.formatMillisecondsAsDate("MMM dd")} • ${audio.audioLengthInSeconds.toDurationMinutes()}"
                        } else {
                            audio.pubDateMillis.formatMillisecondsAsDate("MMM dd")
                        }
                    EmphasisText(
                        text = dateAndDurationText
                    )

                    Spacer(modifier = Modifier.height(Spacing.s16))

                    Row {
                        PrimaryButton(
                            text = playButtonText,
                            height = Spacing.s48
                        ) {
                            detailViewModel.onPlayPauseClicked(audio, isPlaying.toPlayPause())
                            audios?.let {
                                playerViewModel.playPauseAudio(audios, audio)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        soy.gabimoreno.presentation.ui.IconButton(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share),
                            padding = Spacing.s16
                        ) {
                            detailViewModel.onShareClicked(currentContext, audio)
                        }
                    }
                    Spacer(modifier = Modifier.height(Spacing.s16))
                    if (audio is PremiumAudio) {
                        SelectableEmphasisText(
                            text = audio.excerpt.parseFromHtmlFormat()
                        )
                    }
                    Spacer(modifier = Modifier.height(Spacing.s16))
                    if (audio !is PremiumAudio || audio.category != Category.PREMIUM_AUDIO_COURSES) {
                        SelectableEmphasisText(
                            text = audio.description.parseFromHtmlFormat()
                        )
                    }
                }
            }
        }
    }
}
