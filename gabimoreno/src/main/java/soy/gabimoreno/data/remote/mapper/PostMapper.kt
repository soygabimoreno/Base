package soy.gabimoreno.data.remote.mapper

import soy.gabimoreno.data.remote.mapper.category.toSubcategory
import soy.gabimoreno.data.remote.model.post.PostApiModel
import soy.gabimoreno.domain.model.content.Post
import soy.gabimoreno.domain.model.content.findAuthorDisplayNameById
import soy.gabimoreno.domain.util.extractMp3Url
import java.text.SimpleDateFormat
import java.util.Locale

fun List<PostApiModel>.toDomain(): List<Post> = map { it.toDomain() }

fun PostApiModel.toDomain(): Post {
    val renderedContent = contentApiModel.rendered
    val excerpt = excerptApiModel.rendered
    return Post(
        id = id,
        title = titleApiModel.rendered,
        excerpt = excerpt,
        content = renderedContent,
        audioUrl = renderedContent.extractMp3Url(),
        author = findAuthorDisplayNameById(authorId) ?: UNKNOWN_AUTHOR_DISPLAY_NAME,
        subcategoryTitle = categoryIds.toSubcategory()?.title,
        url = url,
        pubDateMillis = dateString.toMillis() ?: EMPTY_PUB_DATE_MILLIS,
        category = categoryIds.toSubcategory(),
    )
}

internal fun String.toMillis(): Long? {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
    val date = formatter.parse(this)
    return date?.time
}

internal const val EMPTY_PUB_DATE_MILLIS = 0L
internal const val UNKNOWN_AUTHOR_DISPLAY_NAME = "Unknown"
