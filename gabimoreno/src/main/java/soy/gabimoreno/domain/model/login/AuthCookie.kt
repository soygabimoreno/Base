package soy.gabimoreno.domain.model.login

data class AuthCookie(
    val status: String,
    val cookie: String,
    val cookieAdmin: String,
    val cookieName: String,
    val user: User
)
