package soy.gabimoreno.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import soy.gabimoreno.R
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit
) {
    Icon(
        Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = stringResource(R.string.back),
        tint = tint,
        modifier = modifier
            .padding(top = Spacing.s8, start = Spacing.s8)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(Spacing.s8)
    )
}
