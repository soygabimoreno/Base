package soy.gabimoreno.presentation.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.ui.BackButton
import soy.gabimoreno.presentation.ui.EmphasisText
import soy.gabimoreno.presentation.ui.EpisodeImage
import soy.gabimoreno.presentation.ui.PrimaryButton
import soy.gabimoreno.util.Resource
import soy.gabimoreno.util.formatMillisecondsAsDate
import soy.gabimoreno.util.toDurationMinutes

@Composable
fun DetailScreen(
    podcastId: String,
) {
    val scrollState = rememberScrollState()
    val podcastSearchViewModel = ViewModelProvider.homeViewModel
    val detailViewModel = ViewModelProvider.detailViewModel
    val playerViewModel = ViewModelProvider.playerViewModel
    val podcast = podcastSearchViewModel.getPodcastDetail(podcastId)
    val currentContext = LocalContext.current

    LaunchedEffect(Unit) {
        detailViewModel.onViewScreen()
    }

    Surface {
        Column(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            Row {
                BackButton()
            }

            if (podcast != null) {
                val playButtonText =
                    if (playerViewModel.podcastIsPlaying &&
                        playerViewModel.currentPlayingEpisode.value?.id == podcast.id
                    ) stringResource(R.string.pause) else stringResource(R.string.play)

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .navigationBarsPadding()
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                        .padding(bottom = if (playerViewModel.currentPlayingEpisode.value != null) 64.dp else 0.dp)

                ) {
                    EpisodeImage(
                        url = podcast.imageUrl,
                        modifier = Modifier.height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        podcast.title,
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        podcast.podcast.author,
                        style = MaterialTheme.typography.body1
                    )

                    EmphasisText(
                        text = "${podcast.pubDateMillis.formatMillisecondsAsDate("MMM dd")} • ${podcast.audioLengthSeconds.toDurationMinutes()}"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        PrimaryButton(
                            text = playButtonText,
                            height = 48.dp
                        ) {
                            detailViewModel.onPlayPauseClicked()
                            playerViewModel.playPauseEpisode(
                                (podcastSearchViewModel.podcastSearch as Resource.Success).data.results,
                                podcast
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        soy.gabimoreno.presentation.ui.IconButton(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = stringResource(R.string.share)
                        ) {
                            detailViewModel.sharePodcastEpisode(currentContext, podcast)
                        }

                        soy.gabimoreno.presentation.ui.IconButton(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = stringResource(R.string.source_web)
                        ) {
                            detailViewModel.openListenNotesURL(currentContext, podcast)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    EmphasisText(
                        text = HtmlCompat.fromHtml(
                            podcast.description,
                            FROM_HTML_MODE_COMPACT
                        ).toString()
                    )
                }
            }
        }
    }
}
