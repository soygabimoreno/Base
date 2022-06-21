package soy.gabimoreno.framework

val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

fun String.camelToSnakeUpperCase(): String {
    return camelRegex.replace(this) {
        "_${it.value}"
    }.uppercase()
}
