package soy.gabimoreno.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostApiModel(
    @Json(name = "id")
    val id: Long
)
