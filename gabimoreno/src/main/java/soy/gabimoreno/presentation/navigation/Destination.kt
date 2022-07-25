package soy.gabimoreno.presentation.navigation

object Destination {
    const val home = HOME
    const val detail = "$DETAIL/{$KEY_ID}"
    const val webView = "$WEB_VIEW/{$KEY_URL}"

    fun detail(episodeId: String): String = "$DETAIL/$episodeId"
    fun webView(url: String): String = "$WEB_VIEW/$url"
}

private const val HOME = "HOME"
private const val DETAIL = "DETAIL"
private const val WEB_VIEW = "WEB_VIEW"

internal const val KEY_ID = "id"
internal const val KEY_URL = "url"
