package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.data.network.model.post.PostApiModel
import soy.gabimoreno.domain.model.content.Post
import soy.gabimoreno.domain.util.extractMp3Url

fun List<PostApiModel>.toDomain(): List<Post> = map { it.toDomain() }

fun PostApiModel.toDomain(): Post {
    val renderedContent = contentApiModel.rendered
    return Post(
        id = id,
        title = titleApiModel.rendered,
        content = renderedContent,
        audioUrl = renderedContent.extractMp3Url()
    )
}
