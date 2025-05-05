package soy.gabimoreno.core

fun String.removeHtmlTags(): String {
    return replace(Regex("<.*?>"), "").trim()
}
