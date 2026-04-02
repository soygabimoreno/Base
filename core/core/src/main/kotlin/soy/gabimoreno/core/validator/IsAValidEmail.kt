package soy.gabimoreno.core.validator

import java.util.regex.Pattern

fun isAValidEmail(email: String): Boolean = EMAIL_PATTERN.matcher(email).matches()

private val EMAIL_PATTERN =
    Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+",
    )
