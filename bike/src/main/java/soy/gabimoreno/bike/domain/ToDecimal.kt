package soy.gabimoreno.bike.domain

import com.clj.fastble.utils.HexUtil
import java.lang.Integer.parseInt

// ByteArray from 255 is 2.55 seconds

fun ByteArray?.toDecimal(): String {
    val hex = HexUtil.formatHexString(
        this,
        true
    )
    println("toDecimal, hex: $hex")
    println("toDecimal, toInt: ${parseInt(hex, 16)}")

    return parseInt(hex, 16).toString()
}
