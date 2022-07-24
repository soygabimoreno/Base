package soy.gabimoreno.presentation.screen.webview

import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.presentation.navigation.Navigator
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.ui.BackButton

@Composable
fun WebViewScreen(url: String) {
    val webViewViewModel = ViewModelProvider.webViewViewModel
    val navController = Navigator.current

    LaunchedEffect(Unit) {
        webViewViewModel.onViewScreen(url)
    }

    Surface {
        Column(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            Row {
                BackButton {
                    webViewViewModel.onBackClicked(url)
                    navController.navigateUp()
                }
            }
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                AndroidView(
                    factory = {
                        android.webkit.WebView(it).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            webViewClient = WebViewClient()
                            loadUrl(url)
                        }
                    }, update = {
                        it.loadUrl(url)
                    }
                )
            }
        }
    }
}
