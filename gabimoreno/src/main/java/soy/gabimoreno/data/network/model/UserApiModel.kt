package soy.gabimoreno.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserApiModel(
    @Json(name = "id")
    val id: Int,
    @Json(name = "username")
    val username: String,
    @Json(name = "nicename")
    val niceName: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "registered")
    val registered: String,
    @Json(name = "displayname")
    val displayName: String,
    @Json(name = "firstname")
    val firstName: String,
    @Json(name = "lastname")
    val lastName: String,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "capabilities")
    val capabilities: String,
    @Json(name = "avatar")
    val avatar: String?
)
