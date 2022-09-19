package soy.gabimoreno.domain.util

import androidx.annotation.VisibleForTesting

fun String.extractMp3Url(): String? {
    val startIndex = indexOf(PREFIX)
    val suffix = SUFFIX
    val suffixLength = suffix.length
    val endIndex = indexOf(suffix) + suffixLength

    if (startIndex == -1 || endIndex == suffixLength - 1) return null
    return substring(startIndex, endIndex)
}

@VisibleForTesting
internal const val PREFIX = "https://gabimoreno.soy/wp-content/uploads/"
internal const val SUFFIX = ".mp3"
