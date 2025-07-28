@file:Suppress("LongParameterList")

package soy.gabimoreno.data.remote.model.course

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import soy.gabimoreno.data.remote.model.post.ContentApiModel
import soy.gabimoreno.data.remote.model.post.ExcerptApiModel
import soy.gabimoreno.data.remote.model.post.TitleApiModel

@JsonClass(generateAdapter = true)
class CourseApiModel(
    @param:Json(name = "id")
    val id: Long,
    @param:Json(name = "title")
    val titleApiModel: TitleApiModel,
    @param:Json(name = "content")
    val contentApiModel: ContentApiModel,
    @param:Json(name = "excerpt")
    val excerptApiModel: ExcerptApiModel,
    @param:Json(name = "author")
    val authorId: Int,
    @param:Json(name = "categories")
    val categoryIds: List<Int>,
    @param:Json(name = "link")
    val url: String,
    @param:Json(name = "date")
    val dateString: String,
    @param:Json(name = "yoast_head_json")
    val yoastHeadJsonApiModel: YoastHeadJsonApiModel,
)
