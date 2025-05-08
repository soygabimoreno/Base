package soy.gabimoreno.coreview

import org.json.JSONObject

fun decodeUnicodeEscapedText(text: String): String {
    return JSONObject("{\"text\":\"$text\"}").getString("text")
}
