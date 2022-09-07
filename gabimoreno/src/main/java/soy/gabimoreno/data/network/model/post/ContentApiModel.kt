package soy.gabimoreno.data.network.model.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentApiModel(
    @Json(name = "rendered")
    val rendered: String,
)
