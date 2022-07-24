package soy.gabimoreno.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import soy.gabimoreno.R

@Composable
fun BackButton(onClick: () -> Unit) {
    Icon(
        Icons.Default.ArrowBack,
        contentDescription = stringResource(R.string.back),
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}
