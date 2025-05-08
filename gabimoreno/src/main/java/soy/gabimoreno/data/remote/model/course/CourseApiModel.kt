package soy.gabimoreno.data.remote.model.course

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import soy.gabimoreno.data.remote.model.post.ContentApiModel
import soy.gabimoreno.data.remote.model.post.ExcerptApiModel
import soy.gabimoreno.data.remote.model.post.TitleApiModel

@JsonClass(generateAdapter = true)
class CourseApiModel(
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
    @Json(name = "yoast_head_json")
    val yoastHeadJsonApiModel: YoastHeadJsonApiModel,
)
