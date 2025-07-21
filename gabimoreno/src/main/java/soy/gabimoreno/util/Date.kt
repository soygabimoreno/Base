package soy.gabimoreno.util

import android.text.format.DateFormat

fun Long.formatMillisecondsAsDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
    DateFormat.format(pattern, this).toString()
