package soy.gabimoreno.fake

import soy.gabimoreno.domain.model.login.AuthCookie

fun buildAuthCookie() = AuthCookie(
    "status",
    "cookie",
    "cookieAdmin",
    "cookieName",
    buildUser()
)
