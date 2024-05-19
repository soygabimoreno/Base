package soy.gabimoreno.data.remote.model.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostApiModel(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val titleApiModel: TitleApiModel,
    @Json(name = "content")
    val contentApiModel: ContentApiModel,
    @Json(name = "excerpt")
    val excerptApiModel: ExcerptApiModel,
    @Json(name = "author")
    val authorId: Int,
    @Json(name = "categories")
    val categoryIds: List<Int>,
    @Json(name = "link")
    val url: String,
    @Json(name = "date")
    val dateString: String,
)
