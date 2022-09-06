package soy.gabimoreno.data.network.model.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostApiModel(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val titleApiModel: TitleApiModel,
    @Json(name = "content")
    val contentApiModel: ContentApiModel
)
