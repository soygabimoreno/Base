package soy.gabimoreno.core

fun String.findUrl(): String {
    val startIndex = indexOf("https")
    val textFromUrl = substring(startIndex)
    val firstPossibleEndOfUrlIndex = textFromUrl.indexOfOrMaxValue(" ")
    val secondPossibleEndOfUrlIndex = textFromUrl.indexOfOrMaxValue("\n")
    val endOfUrlIndex = kotlin.math.min(
        firstPossibleEndOfUrlIndex,
        secondPossibleEndOfUrlIndex
    ) + startIndex
    return substring(startIndex, endOfUrlIndex)
}

private fun String.indexOfOrMaxValue(delimiter: String): Int = run {
    val index = indexOf(delimiter)
    if (index == -1) {
        Int.MAX_VALUE
    } else {
        index
    }
}
