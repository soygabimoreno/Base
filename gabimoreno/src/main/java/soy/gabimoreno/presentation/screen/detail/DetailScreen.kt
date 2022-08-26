package soy.gabimoreno.presentation.screen.detail

import androidx.compose.foundation.layout.*
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
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.data.tracker.domain.toPlayPause
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.HomeViewModel
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.BackButton
import soy.gabimoreno.presentation.ui.EmphasisText
import soy.gabimoreno.presentation.ui.EpisodeImage
import soy.gabimoreno.presentation.ui.PrimaryButton
import soy.gabimoreno.util.formatMillisecondsAsDate
import soy.gabimoreno.util.toDurationMinutes

@Composable
fun DetailScreen(
    podcastId: String,
    onBackClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val podcastSearchViewModel = ViewModelProvider.homeViewModel
    val detailViewModel = ViewModelProvider.detailViewModel
    val playerViewModel = ViewModelProvider.playerViewModel
    val episode = podcastSearchViewModel.getPodcastDetail(podcastId)
    val currentContext = LocalContext.current

    LaunchedEffect(Unit) {
        episode?.let {
            detailViewModel.onViewScreen(episode)
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            Row {
                BackButton {
                    episode?.let {
                        detailViewModel.onBackClicked(episode)
                    }
                    onBackClicked()
                }
            }

            if (episode != null) {
                val isPlaying = playerViewModel.podcastIsPlaying &&
                    playerViewModel.currentPlayingEpisode.value?.id == episode.id
                val playButtonText =
                    if (isPlaying) stringResource(R.string.pause) else stringResource(R.string.play)

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .navigationBarsPadding()
                        .padding(vertical = Spacing.s24, horizontal = Spacing.s16)
                        .padding(bottom = if (playerViewModel.currentPlayingEpisode.value != null) Spacing.s64 else Spacing.s0)

                ) {
                    EpisodeImage(
                        url = episode.imageUrl,
                        modifier = Modifier.height(Spacing.oddSpacing120)
                    )

                    Spacer(modifier = Modifier.height(Spacing.s32))

                    Text(
                        episode.title,
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(Spacing.s24))

                    Text(
                        episode.podcast.author,
                        style = MaterialTheme.typography.body1
                    )

                    EmphasisText(
                        text = "${episode.pubDateMillis.formatMillisecondsAsDate("MMM dd")} • ${episode.audioLengthInSeconds.toDurationMinutes()}"
                    )

                    Spacer(modifier = Modifier.height(Spacing.s16))

                    Row {
                        PrimaryButton(
                            text = playButtonText,
                            height = Spacing.s48
                        ) {
                            detailViewModel.onPlayPauseClicked(episode, isPlaying.toPlayPause())
                            playerViewModel.playPauseEpisode(
                                (podcastSearchViewModel.podcastSearch as HomeViewModel.ViewState.Content).data.results,
                                episode
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        soy.gabimoreno.presentation.ui.IconButton(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share),
                            padding = Spacing.s16
                        ) {
                            detailViewModel.onShareClicked(currentContext, episode)
                        }
                    }

                    Spacer(modifier = Modifier.height(Spacing.s16))

                    EmphasisText(
                        text = HtmlCompat.fromHtml(
                            episode.description,
                            FROM_HTML_MODE_COMPACT
                        ).toString()
                    )
                }
            }
        }
    }
}
