package soy.gabimoreno.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JwtAuthApiModel(
    @Json(name = "token")
    val token: String,
    @Json(name = "user_email")
    val userEmail: String,
    @Json(name = "user_nicename")
    val userNiceName: String,
    @Json(name = "user_display_name")
    val userDisplayName: String,
)
