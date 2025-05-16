package soy.gabimoreno.presentation.ui.button

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SecondaryButton(
    text: String,
    height: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    CommonButton(
        text = text,
        height = height,
        modifier = modifier,
        background = MaterialTheme.colors.secondary,
        onClick = onClick
    )
}
