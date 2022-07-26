package soy.gabimoreno

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.ProvideWindowInsets
import soy.gabimoreno.presentation.screen.ProvideMultiViewModel
import soy.gabimoreno.presentation.screen.player.PlayerScreen
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.ui.AudioBottomBar

@Composable
fun GabiMorenoApp(
    backDispatcher: OnBackPressedDispatcher
) {
    GabiMorenoTheme {
        ProvideWindowInsets {
            ProvideMultiViewModel {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AppNavigation()
                        AudioBottomBar(
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                        PlayerScreen(backDispatcher)
                    }
                }
        }
    }
}
