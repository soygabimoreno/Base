package soy.gabimoreno.data.remote.mapper

import soy.gabimoreno.data.remote.model.AuthCookieApiModel
import soy.gabimoreno.domain.model.login.AuthCookie

fun AuthCookieApiModel.toDomain() = AuthCookie(
    status = status,
    cookie = cookie,
    cookieAdmin = cookieAdmin,
    cookieName = cookieName,
    user = userApiModel.toDomain(),
)
