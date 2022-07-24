package soy.gabimoreno.presentation.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
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
import soy.gabimoreno.data.tracker.domain.toPlayPause
import soy.gabimoreno.presentation.navigation.Navigator
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.screen.home.HomeViewModel
import soy.gabimoreno.presentation.ui.BackButton
import soy.gabimoreno.presentation.ui.EmphasisText
import soy.gabimoreno.presentation.ui.EpisodeImage
import soy.gabimoreno.presentation.ui.PrimaryButton
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
    val episode = podcastSearchViewModel.getPodcastDetail(podcastId)
    val currentContext = LocalContext.current
    val navController = Navigator.current

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
                    navController.navigateUp()
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
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                        .padding(bottom = if (playerViewModel.currentPlayingEpisode.value != null) 64.dp else 0.dp)

                ) {
                    EpisodeImage(
                        url = episode.imageUrl,
                        modifier = Modifier.height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        episode.title,
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        episode.podcast.author,
                        style = MaterialTheme.typography.body1
                    )

                    EmphasisText(
                        text = "${episode.pubDateMillis.formatMillisecondsAsDate("MMM dd")} • ${episode.audioLengthSeconds.toDurationMinutes()}"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        PrimaryButton(
                            text = playButtonText,
                            height = 48.dp
                        ) {
                            detailViewModel.onPlayPauseClicked(episode, isPlaying.toPlayPause())
                            playerViewModel.playPauseEpisode(
                                (podcastSearchViewModel.podcastSearch as HomeViewModel.ViewState.Content).data.results,
                                episode
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        soy.gabimoreno.presentation.ui.IconButton(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = stringResource(R.string.share),
                            padding = 16.dp
                        ) {
                            detailViewModel.onShareClicked(currentContext, episode)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
