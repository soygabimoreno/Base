package soy.gabimoreno.ui.screen.podcast

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.R
import soy.gabimoreno.ui.common.BackButton
import soy.gabimoreno.ui.common.EmphasisText
import soy.gabimoreno.ui.common.PrimaryButton
import soy.gabimoreno.ui.common.ViewModelProvider
import soy.gabimoreno.util.Resource
import soy.gabimoreno.util.formatMillisecondsAsDate
import soy.gabimoreno.util.toDurationMinutes

@Composable
fun PodcastDetailScreen(
    podcastId: String,
) {
    val scrollState = rememberScrollState()
    val podcastSearchViewModel = ViewModelProvider.podcastSearch
    val detailViewModel = ViewModelProvider.podcastDetail
    val playerViewModel = ViewModelProvider.podcastPlayer
    val podcast = podcastSearchViewModel.getPodcastDetail(podcastId)
    val currentContext = LocalContext.current

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
                    PodcastImage(
                        url = podcast.image,
                        modifier = Modifier.height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        podcast.titleOriginal,
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        podcast.podcast.publisherOriginal,
                        style = MaterialTheme.typography.body1
                    )

                    EmphasisText(
                        text = "${podcast.pubDateMS.formatMillisecondsAsDate("MMM dd")} • ${podcast.audioLengthSec.toDurationMinutes()}"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        PrimaryButton(
                            text = playButtonText,
                            height = 48.dp
                        ) {
                            playerViewModel.playPodcast(
                                (podcastSearchViewModel.podcastSearch as Resource.Success).data.results,
                                podcast
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        soy.gabimoreno.ui.common.IconButton(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = stringResource(R.string.share)
                        ) {
                            detailViewModel.sharePodcastEpisode(currentContext, podcast)
                        }

                        soy.gabimoreno.ui.common.IconButton(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = stringResource(R.string.source_web)
                        ) {
                            detailViewModel.openListenNotesURL(currentContext, podcast)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    EmphasisText(text = podcast.descriptionOriginal)
                }
            }
        }
    }
}
