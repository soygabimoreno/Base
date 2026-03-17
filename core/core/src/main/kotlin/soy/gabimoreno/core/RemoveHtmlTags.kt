package soy.gabimoreno.core

fun String.removeHtmlTags(): String = replace(Regex("<.*?>"), "").trim()
