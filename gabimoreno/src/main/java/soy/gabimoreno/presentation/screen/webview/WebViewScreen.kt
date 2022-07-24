package soy.gabimoreno.presentation.screen.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.statusBarsPadding
import soy.gabimoreno.presentation.navigation.Navigator
import soy.gabimoreno.presentation.screen.ViewModelProvider
import soy.gabimoreno.presentation.ui.BackButton

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String) {
    val webViewViewModel = ViewModelProvider.webViewViewModel
    val navController = Navigator.current

    LaunchedEffect(Unit) {
        webViewViewModel.onViewScreen(url)
    }

    var webView: WebView? = null
    var webViewBackEnabled by remember { mutableStateOf(false) }

    Surface {
        Column(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            Row {
                BackButton {
                    if (webViewBackEnabled) {
                        webView?.goBack()
                    } else {
                        webViewViewModel.onBackClicked(url)
                        navController.navigateUp()
                    }
                }
            }
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                AndroidView(
                    factory = {
                        WebView(it).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(
                                    webView: WebView,
                                    url: String?,
                                    favicon: Bitmap?
                                ) {
                                    url?.let {
                                        webViewViewModel.onPageStarted(url)
                                    }
                                    webViewBackEnabled = webView.canGoBack()
                                }
                            }

                            settings.javaScriptEnabled = true
                            loadUrl(url)
                            webView = this
                        }
                    }, update = {
                        webView = it
                    }
                )
            }
        }
    }
}
