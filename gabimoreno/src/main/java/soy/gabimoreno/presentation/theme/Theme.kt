package soy.gabimoreno.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Orange,
    secondary = Green,
    background = PurpleDark,
    surface = PurpleDark,
    onPrimary = White,
    onBackground = White,
    onSurface = White,
)

@Composable
fun GabiMorenoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
