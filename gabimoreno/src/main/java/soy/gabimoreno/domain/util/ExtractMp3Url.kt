package soy.gabimoreno.domain.util

fun String.extractMp3Url(): String? {
    val startIndex = indexOf(PREFIX)
    val suffix = SUFFIX
    val suffixLength = suffix.length
    var endIndex = indexOf(suffix) + suffixLength
    if (endIndex <= startIndex) {
        endIndex += substring(endIndex, length).indexOf(suffix) + suffixLength
    }

    if (startIndex == -1 || endIndex == suffixLength - 1) return null
    return substring(startIndex, endIndex)
}

private const val PREFIX = "https://gabimoreno.soy/wp-content/uploads/"
private const val SUFFIX = ".mp3"
