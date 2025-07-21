package soy.gabimoreno.presentation

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import soy.gabimoreno.presentation.navigation.AppBottomNavigation
import soy.gabimoreno.presentation.navigation.AppNavigation
import soy.gabimoreno.presentation.navigation.navigatePoppingUpToStartDestination
import soy.gabimoreno.presentation.screen.ProvideMultiViewModel
import soy.gabimoreno.presentation.screen.auth.AuthBottomSheetController
import soy.gabimoreno.presentation.screen.auth.AuthModalBottomSheetRoot
import soy.gabimoreno.presentation.screen.player.PlayerScreen
import soy.gabimoreno.presentation.screen.review.ReviewDialog
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.ui.AudioBottomBar

@Composable
fun AppUi(
    backDispatcher: OnBackPressedDispatcher,
    appState: AppState = rememberAppState(),
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route.orEmpty()
    val stateModalBottomSheet = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val authBottomSheetController =
        remember { AuthBottomSheetController(stateModalBottomSheet, coroutineScope) }

    GabiMorenoScreen {
        ProvideWindowInsets {
            ProvideMultiViewModel {
                AuthModalBottomSheetRoot(
                    modalBottomSheetState = stateModalBottomSheet,
                    onHideBottomSheet = { authBottomSheetController.hide() },
                ) {
                    Scaffold(
                        bottomBar = {
                            AppBottomNavigation(currentRoute) { item ->
                                navController.navigatePoppingUpToStartDestination(
                                    item.navCommand.route,
                                )
                            }
                        },
                    ) { padding ->
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxHeight()
                                    .padding(padding),
                        ) {
                            AppNavigation(
                                navController = navController,
                                appState = appState,
                                onRequireAuth = { authBottomSheetController.show() },
                            )
                            AudioBottomBar(
                                modifier = Modifier.align(Alignment.BottomCenter),
                            )
                        }
                    }
                }
                Box {
                    PlayerScreen(backDispatcher)
                    ReviewDialog()
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
            color = MaterialTheme.colors.background,
        ) {
            content()
        }
    }
}
