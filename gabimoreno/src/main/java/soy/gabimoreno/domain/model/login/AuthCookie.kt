package soy.gabimoreno.domain.model.login

import androidx.annotation.VisibleForTesting

data class AuthCookie(
    val status: String,
    val cookie: String,
    val cookieAdmin: String,
    val cookieName: String,
    val user: User
) {
    fun isStatusOK() = status == STATUS_OK
}

@VisibleForTesting
internal const val STATUS_OK = "ok"
