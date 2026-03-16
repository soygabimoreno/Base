package soy.gabimoreno.coreview

import org.json.JSONObject

fun decodeUnicodeEscapedText(text: String): String =
    JSONObject("{\"text\":\"$text\"}").getString("text")
