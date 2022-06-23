package soy.gabimoreno.core

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

fun String.camelToSnakeUpperCase(): String {
    return camelRegex.replace(this) {
        "_${it.value}"
    }.uppercase()
}
