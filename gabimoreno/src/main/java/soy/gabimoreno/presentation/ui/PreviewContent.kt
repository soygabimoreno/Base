package soy.gabimoreno.presentation.ui

import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import soy.gabimoreno.presentation.navigation.ProvideNavHostController
import soy.gabimoreno.presentation.theme.GabiMorenoTheme

@Composable
fun PreviewContent(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    GabiMorenoTheme(darkTheme = darkTheme) {
        ProvideWindowInsets {
            ProvideNavHostController {
                content()
            }
        }
    }
}
