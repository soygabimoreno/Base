package soy.gabimoreno.data.remote.model.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExcerptApiModel(
    @Json(name = "rendered")
    val rendered: String,
)
