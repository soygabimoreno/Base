package soy.gabimoreno.presentation.ui.button

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
fun PrimaryButton(
    text: String,
    height: Dp,
    onClick: () -> Unit,
) {
    CommonButton(
        text = text,
        height = height,
        background = MaterialTheme.colors.primary,
        onClick = onClick
    )
}
