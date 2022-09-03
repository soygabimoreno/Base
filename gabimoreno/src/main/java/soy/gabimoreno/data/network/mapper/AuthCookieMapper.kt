package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.data.network.model.AuthCookieApiModel
import soy.gabimoreno.domain.model.login.AuthCookie

fun AuthCookieApiModel.toDomain() = AuthCookie(
    status = status,
    cookie = cookie,
    cookieAdmin = cookieAdmin,
    cookieName = cookieName,
    user = userApiModel.toDomain()
)
