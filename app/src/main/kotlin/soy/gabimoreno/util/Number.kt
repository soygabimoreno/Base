package soy.gabimoreno.util

fun Int.toDurationMinutes(): String {
    val minutes = (this / MINUTES_IN_HOUR)
    return "$minutes min"
}

private const val MINUTES_IN_HOUR = 60
