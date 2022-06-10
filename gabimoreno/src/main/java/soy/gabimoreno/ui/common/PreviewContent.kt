package soy.gabimoreno.ui.common

import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import soy.gabimoreno.ui.navigation.ProvideNavHostController
import soy.gabimoreno.ui.theme.GabiMorenoTheme

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
