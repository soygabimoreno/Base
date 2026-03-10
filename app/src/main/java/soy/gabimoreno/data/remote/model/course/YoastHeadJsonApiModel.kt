package soy.gabimoreno.data.remote.model.course

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoastHeadJsonApiModel(
    @Json(name = "og_image")
    val ogImage: List<OgImageApiModel>,
)

@JsonClass(generateAdapter = true)
data class OgImageApiModel(
    @Json(name = "url")
    val url: String,
)
