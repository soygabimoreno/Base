package soy.gabimoreno.core.validator

fun isAValidPassword(password: String): Boolean = password.length >= MINIMUM_PASSWORD_LENGTH

private const val MINIMUM_PASSWORD_LENGTH = 4
