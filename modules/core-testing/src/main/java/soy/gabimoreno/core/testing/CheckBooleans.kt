package soy.gabimoreno.core.testing

import io.kotest.data.blocking.forAll
import io.kotest.data.row

fun checkBoolean(callback: (Boolean) -> Unit) {
    forAll(row(true), row(false)) {
        callback(it)
    }
}

fun check2Booleans(callback: (Boolean, Boolean) -> Unit) {
    forAll(
        row(TruthTable(first = true, second = true)),
        row(TruthTable(first = true, second = false)),
        row(TruthTable(first = false, second = true)),
        row(TruthTable(first = false, second = false)),
    ) {
        callback(it.first, it.second)
    }
}

private data class TruthTable(
    val first: Boolean,
    val second: Boolean,
)
