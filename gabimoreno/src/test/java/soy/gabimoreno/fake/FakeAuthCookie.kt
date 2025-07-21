package soy.gabimoreno.fake

import soy.gabimoreno.domain.model.login.AuthCookie
import soy.gabimoreno.domain.model.login.STATUS_OK

fun buildAuthCookie() =
    AuthCookie(
        status = STATUS_OK,
        cookie = "cookie",
        cookieAdmin = "cookieAdmin",
        cookieName = "cookieName",
        user = buildUser(),
    )
