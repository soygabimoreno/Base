package soy.gabimoreno.domain.util

fun String.extractMp3Url(): String? {
    val startIndex = indexOf("https://")
    val suffix = ".mp3"
    val suffixLength = suffix.length
    val endIndex = indexOf(suffix) + suffixLength

    if (startIndex == -1 || endIndex == suffixLength - 1) return null
    return substring(startIndex, endIndex)
}
