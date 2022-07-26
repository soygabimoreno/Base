package soy.gabimoreno.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun PrimaryButton(
    text: String,
    height: Dp = Spacing.oddSpacing58,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .defaultMinSize(Spacing.oddSpacing200)
            .height(height)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(horizontal = Spacing.s16)
                .align(Alignment.Center)
        )
    }
}
