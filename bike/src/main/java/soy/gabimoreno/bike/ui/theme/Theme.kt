package soy.gabimoreno.bike.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val DarkColorPalette =
    darkColors(
        primary = Purple200,
        primaryVariant = Purple700,
        secondary = Teal200,
    )

@Composable
fun BaseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
