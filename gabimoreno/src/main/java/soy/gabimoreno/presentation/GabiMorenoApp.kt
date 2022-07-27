package soy.gabimoreno.presentation

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.ProvideWindowInsets
import soy.gabimoreno.presentation.navigation.NavItem
import soy.gabimoreno.presentation.screen.ProvideMultiViewModel
import soy.gabimoreno.presentation.screen.player.PlayerScreen
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.ui.AudioBottomBar

@Composable
fun GabiMorenoApp(
    backDispatcher: OnBackPressedDispatcher
) {
    GabiMorenoScreen {
        ProvideWindowInsets {
            ProvideMultiViewModel {
                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            NavItem.values().forEach { item ->
                                val title = stringResource(id = item.titleResId)
                                BottomNavigationItem(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = title
                                        )
                                    },
                                    label = {
                                        Text(text = title)
                                    }
                                )
                            }
                        }
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
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
}

@Composable
fun GabiMorenoScreen(content: @Composable () -> Unit) {
    GabiMorenoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}
