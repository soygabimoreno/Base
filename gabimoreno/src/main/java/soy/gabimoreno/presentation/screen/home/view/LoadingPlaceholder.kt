package soy.gabimoreno.presentation.screen.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.StaggeredVerticalGrid

@Composable
fun LoadingPlaceholder() {
    StaggeredVerticalGrid(
        crossAxisCount = 2,
        spacing = Spacing.s16,
        modifier = Modifier.padding(horizontal = Spacing.s16),
    ) {
        (1..10).map {
            Column(
                modifier =
                    Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colors.background),
            ) {
                Box(
                    Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .aspectRatio(1f)
                        .background(MaterialTheme.colors.onBackground.copy(alpha = 0.08f)),
                )
                Box(
                    modifier =
                        Modifier
                            .padding(Spacing.s8)
                            .background(MaterialTheme.colors.onBackground.copy(alpha = 0.08f)),
                )
            }
        }
    }
}
