package soy.gabimoreno.presentation.ui.button

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun PrimaryButton(
    text: String,
    height: Dp,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    if (isEnabled) {
        CommonButton(
            text = text,
            height = height,
            background = MaterialTheme.colors.primary,
            modifier = modifier,
            onClick = onClick,
        )
    } else {
        CommonButton(
            text = text,
            height = height,
            background = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            modifier = modifier,
            onClick = {},
        )
    }
}
