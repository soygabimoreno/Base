package soy.gabimoreno.util

fun Int.toDurationMinutes(): String {
    val minutes = (this / 60)
    return "$minutes min"
}
