package soy.gabimoreno.framework

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY

fun
    String.parseFromHtmlFormat() =
    HtmlCompat.fromHtml(
        this,
        FROM_HTML_MODE_LEGACY
    ).toString()
