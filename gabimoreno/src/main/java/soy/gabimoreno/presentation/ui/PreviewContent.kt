package soy.gabimoreno.presentation.ui

import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import soy.gabimoreno.presentation.theme.GabiMorenoTheme

@Composable
fun PreviewContent(
    content: @Composable () -> Unit
) {
    GabiMorenoTheme {
        ProvideWindowInsets {
            content()
        }
    }
}
