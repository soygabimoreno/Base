package soy.gabimoreno.presentation.screen.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.AudioImage

@Composable
fun EpisodeView(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.background)
                .clickable(onClick = onClick),
    ) {
        AudioImage(
            url = episode.thumbnailUrl,
            aspectRatio = 1f,
        )
        Text(
            episode.title,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(Spacing.s8),
        )
    }
}
