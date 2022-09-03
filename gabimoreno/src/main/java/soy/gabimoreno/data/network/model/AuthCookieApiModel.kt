package soy.gabimoreno.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthCookieApiModel(
    @Json(name = "status")
    val status: String,
    @Json(name = "cookie")
    val cookie: String,
    @Json(name = "cookie_admin")
    val cookieAdmin: String,
    @Json(name = "cookie_name")
    val cookieName: String,
    @Json(name = "user")
    val userApiModel: UserApiModel
)
