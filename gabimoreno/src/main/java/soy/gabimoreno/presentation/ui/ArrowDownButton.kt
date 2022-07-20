package soy.gabimoreno.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import soy.gabimoreno.R

@Composable
fun ArrowDownButton(
    onClick: () -> Unit
) {
    Icon(
        imageVector = Icons.Rounded.KeyboardArrowDown,
        contentDescription = stringResource(R.string.close),
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}
