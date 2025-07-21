package soy.gabimoreno.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import soy.gabimoreno.R
import soy.gabimoreno.presentation.theme.Spacing

@Composable
fun ArrowDownButton(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = stringResource(R.string.close),
        modifier =
            Modifier
                .padding(top = Spacing.s8, start = Spacing.s8)
                .clip(CircleShape)
                .clickable(onClick = onClick)
                .padding(Spacing.s8),
    )
}
